/*
 * Vulnerable Client-Server Application (VuCSA)
 *
 * Copyright (C) 2021 Michal Válka
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.warxim.vucsa.server.challenge.sqlinjection;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vulnerable SQL Database for SQL Injection challenge.
 */
@Getter
public class SqlDatabase implements AutoCloseable {
    private Connection connection;

    /**
     * Constructs database with given url.
     * @param url Path to database file
     */
    public SqlDatabase(String url) throws SQLException {
        connection = DriverManager.getConnection(url);

        init();
        autofillDatabase();
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.SEVERE, "Could not close database connection!", e);
            }
        }
    }

    /**
     * Obtain result by executing query.
     * <p>Totally wrong and insecure use of SQLite database.</p>
     * @param sql SQL to be executed
     * @return Result of given generic type
     */
    public <R> Optional<R> selectOne(String sql, Function<ResultSet, R> transformation) {
        try (var stmt = connection.createStatement()) {
            var resultSet = stmt.executeQuery(sql);
            return Optional.ofNullable(transformation.apply(resultSet));
        } catch (SQLException e) {
            logQueryException(e);
        }
        return Optional.empty();
    }

    /**
     *
     * <p>Totally wrong and insecure use of SQLite database.</p>
     * @param sql SQL to be executed
     * @return Result list of given generic type
     */
    public <R> Optional<List<R>> selectList(String sql, Function<ResultSet, R> transformation) {
        try (var stmt = connection.createStatement()) {
            var results = new LinkedList<R>();
            var resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                results.add(transformation.apply(resultSet));
            }
            return Optional.of(results);
        } catch (SQLException e) {
            logQueryException(e);
        }
        return Optional.empty();
    }

    /**
     * Totally wrong and insecure use of SQLite database.
     * @param sql SQL to be executed
     * @return {@code true} if the execution was successfull
     */
    public boolean execute(String sql) {
        try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            logQueryException(e);
        }
        return false;
    }

    /**
     * Creates all history tables.
     */
    private void init() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS food ("
                    + "id INTEGER PRIMARY KEY,"
                    + "name TEXT NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "price REAL NOT NULL"
                    + ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS user ("
                    + "id INTEGER PRIMARY KEY,"
                    + "email TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "name TEXT NOT NULL"
                    + ");");
        }
    }

    /**
     * Automatically fills database with data.
     */
    private void autofillDatabase() {
        Function<ResultSet, Integer> countTransformer = resultSet -> {
            try {
                return resultSet.getInt(1);
            } catch (SQLException throwables) {
                return null;
            }
        };

        var maybeCount = selectOne("SELECT COUNT(*) FROM food", countTransformer);
        if (maybeCount.isPresent() && maybeCount.get() == 0) {
            execute("INSERT INTO food (id, name, description, price) VALUES (1, 'Pizza Margherita', 'Tomato sauce, Mozzarella, Basil, Oregano', 6.50);");
            execute("INSERT INTO food (id, name, description, price) VALUES (2, 'Pizza Hawaii', 'Cream, Mozzarella, Ham, Pineapple', 7.50);");
            execute("INSERT INTO food (id, name, description, price) VALUES (3, 'Pizza Salami', 'Tomato sauce, Mozzarella, Salami', 8.50);");
            execute("INSERT INTO food (id, name, description, price) VALUES (4, 'BBQ Burger', 'Ground beef, Bacon, Cheddar, BBQ sauce, Onion, Lettuce', 9.49);");
        }

        maybeCount = selectOne("SELECT COUNT(*) FROM user", countTransformer);
        if (maybeCount.isPresent() && maybeCount.get() == 0) {
            execute("INSERT INTO user (id, email, password, name) VALUES (1, 'admin@petep.com', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'Admin Nimda');");
            execute("INSERT INTO user (id, email, password, name) VALUES (2, 'john@example.com', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 'John Example');");
            execute("INSERT INTO user (id, email, password, name) VALUES (3, 'hitchhiker@example.com', '92cfceb39d57d914ed8b14d0e37643de0797ae56', 'The Hitchhiker');");
        }
    }

    private void logQueryException(SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, "SQL exception occurred during query execution!", e);
    }
}
