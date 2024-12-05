PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS "users"
(
    "username"     VARCHAR NOT NULL UNIQUE,
    "name"         VARCHAR NOT NULL,
    "pwd_hash"     VARCHAR NOT NULL,
    "email"        VARCHAR NOT NULL UNIQUE,
    "role"         VARCHAR NOT NULL,
    "creation"     DATE    NOT NULL,
    "notification" TEXT,
    PRIMARY KEY ("username")
);

CREATE INDEX IF NOT EXISTS "users_index_0" ON "users" ("username");
CREATE INDEX IF NOT EXISTS "users_index_1" ON "users" ("name");
CREATE INDEX IF NOT EXISTS "users_index_2" ON "users" ("email");

CREATE TABLE IF NOT EXISTS "books"
(
    "isbn"        VARCHAR NOT NULL UNIQUE,
    "name"        VARCHAR NOT NULL,
    "qty"         INTEGER NOT NULL CHECK ("qty" > 0),
    "description" TEXT,
    "author"      VARCHAR,
    "category"    VARCHAR,
    "thumbnail"   BLOB,
    PRIMARY KEY ("isbn")
);

CREATE INDEX IF NOT EXISTS "books_index_0" ON "books" ("name");
CREATE INDEX IF NOT EXISTS "books_index_1" ON "books" ("author");
CREATE INDEX IF NOT EXISTS "books_index_2" ON "books" ("category");

CREATE TABLE IF NOT EXISTS "borrow"
(
    "id"          INTEGER NOT NULL UNIQUE,
    "isbn"        VARCHAR NOT NULL,
    "username"    VARCHAR NOT NULL,
    "from"        DATE    NOT NULL,
    "due"         DATE    NOT NULL,
    "return_date" DATE,
    "comment"     TEXT,
    "rating"      INTEGER NOT NULL DEFAULT 0 CHECK ("rating" >= 0 AND "rating" <= 5),
    PRIMARY KEY ("id"),
    FOREIGN KEY ("username") REFERENCES "users" ("username")
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    FOREIGN KEY ("isbn") REFERENCES "books" ("isbn")
        ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS "borrow_index_0" ON "borrow" ("isbn");
CREATE INDEX IF NOT EXISTS "borrow_index_1" ON "borrow" ("username");

CREATE TABLE IF NOT EXISTS "borrow_reqs"
(
    "id"       INTEGER NOT NULL UNIQUE,
    "isbn"     VARCHAR NOT NULL,
    "username" VARCHAR NOT NULL,
    "duration" INTEGER NOT NULL CHECK ("duration" > 0 AND "duration" <= 30),
    PRIMARY KEY ("id"),
    FOREIGN KEY ("isbn") REFERENCES "books" ("isbn")
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    FOREIGN KEY ("username") REFERENCES "users" ("username")
        ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS "borrow_reqs_index_0" ON "borrow_reqs" ("isbn");
CREATE INDEX IF NOT EXISTS "borrow_reqs_index_1" ON "borrow_reqs" ("username");

INSERT OR IGNORE INTO "users" ("username", "name", "pwd_hash", "email", "role", "creation")
       VALUES ('root', 'SYSTEM', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'root@localhost' , 'ADMINISTRATOR', datetime('now'));
