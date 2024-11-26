PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS "readers" (
    "id" INTEGER NOT NULL UNIQUE,
    "name" VARCHAR(255) NOT NULL,
    "password_hash" VARCHAR(255) NOT NULL,
    "registration_time" TIMESTAMP NOT NULL,
    "expiration_time" TIMESTAMP NOT NULL,
    "email" VARCHAR(255) UNIQUE NOT NULL,
    "phone_number" VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY("id")
);

CREATE INDEX IF NOT EXISTS "readers_index_0" ON "readers" ("name");
CREATE INDEX IF NOT EXISTS "readers_index_1" ON "readers" ("email");
CREATE INDEX IF NOT EXISTS "readers_index_2" ON "readers" ("phone_number");

CREATE TABLE IF NOT EXISTS "documents" (
    "id" INTEGER NOT NULL UNIQUE,
    "name" VARCHAR(255) NOT NULL,
    "quantity" INTEGER NOT NULL DEFAULT 1,
    "author" VARCHAR(255) NOT NULL,
    "publisher" VARCHAR(255) NOT NULL,
    "category" VARCHAR(255) NOT NULL,
    PRIMARY KEY("id")
);

CREATE INDEX IF NOT EXISTS "documents_index_0" ON "documents" ("name");
CREATE INDEX IF NOT EXISTS "documents_index_1" ON "documents" ("author");
CREATE INDEX IF NOT EXISTS "documents_index_2" ON "documents" ("publisher");
CREATE INDEX IF NOT EXISTS "documents_index_3" ON "documents" ("category");

CREATE TABLE IF NOT EXISTS "borrowings" (
    "id" INTEGER NOT NULL UNIQUE,
    "reader_id" INTEGER NOT NULL,
    "document_id" INTEGER NOT NULL,
    "borrow_time" TIMESTAMP NOT NULL,
    "due_time" TIMESTAMP NOT NULL,
    "return_time" TIMESTAMP NOT NULL DEFAULT -1,
    PRIMARY KEY("id"),

    FOREIGN KEY ("document_id") REFERENCES "documents"("id") ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY ("reader_id") REFERENCES "readers"("id") ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS "borrowings_index_0" ON "borrowings" ("reader_id");
CREATE INDEX IF NOT EXISTS "borrowings_index_1" ON "borrowings" ("document_id");