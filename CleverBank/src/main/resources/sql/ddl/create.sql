CREATE SCHEMA IF NOT EXISTS bank;

CREATE SEQUENCE IF NOT EXISTS banks_seq
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    INCREMENT BY 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS accounts_seq
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    INCREMENT BY 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS users_seq
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    INCREMENT BY 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS transactions_seq
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    INCREMENT BY 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS banks
(
    id   INTEGER PRIMARY KEY  DEFAULT nextval('banks_seq'),
    name VARCHAR(255) NOT NULL,
    bic  VARCHAR(11)  NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER PRIMARY KEY  DEFAULT nextval('users_seq'),
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts
(
    id      INTEGER PRIMARY KEY     DEFAULT nextval('accounts_seq'),
    number  VARCHAR(20)    NOT NULL,
    balance NUMERIC(15, 2) NOT NULL,
    bank_id INTEGER        NOT NULL REFERENCES banks (id),
    user_id INTEGER        NOT NULL REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS transactions
(
    id              INTEGER PRIMARY KEY DEFAULT nextval('transactions_seq'),
    from_account_id INTEGER        NOT NULL REFERENCES accounts (id),
    to_account_id   INTEGER        NOT NULL REFERENCES accounts (id),
    amount          NUMERIC(15, 2) NOT NULL,
    date            TIMESTAMP      NOT NULL
);