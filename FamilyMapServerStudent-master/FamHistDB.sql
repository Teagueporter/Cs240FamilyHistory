BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Person" (
	"personID"	TEXT NOT NULL UNIQUE,
	"associatedUsername"	TEXT NOT NULL,
	"firstName"	TEXT NOT NULL,
	"lastName"	TEXT NOT NULL,
	"gender"	TEXT NOT NULL,
	"fatherID"	TEXT,
	"motherID"	TEXT,
	"spouseID"	TEXT
);
CREATE TABLE IF NOT EXISTS "AuthToken" (
	"authtoken"	TEXT NOT NULL UNIQUE,
	"username"	TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "User" (
	"username"	TEXT NOT NULL UNIQUE,
	"password"	TEXT NOT NULL,
	"email"	TEXT NOT NULL,
	"firstName"	TEXT NOT NULL,
	"lastName"	TEXT NOT NULL,
	"gender"	TEXT NOT NULL,
	"personID"	TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "Event" (
	"eventID"	TEXT NOT NULL UNIQUE,
	"associatedUsername"	TEXT NOT NULL,
	"personID"	TEXT NOT NULL,
	"latitude"	REAL NOT NULL,
	"longitude"	REAL NOT NULL,
	"country"	TEXT NOT NULL,
	"city"	TEXT NOT NULL,
	"eventType"	TEXT NOT NULL,
	"year"	INTEGER NOT NULL
);
COMMIT;
