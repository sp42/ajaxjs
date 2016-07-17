CREATE TABLE "BASE" (
    "id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
    "uid" VARCHAR UNIQUE , 
    "name" VARCHAR, 
    "createDate" DATETIME, 
    "updateDate" DATETIME, 
    "content" VARCHAR
);

CREATE TABLE "NEWS" (
    "id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
    "uid" VARCHAR UNIQUE , 
    "name" VARCHAR, 
    "createDate" DATETIME, 
    "updateDate" DATETIME, 
    "content" VARCHAR,
    "intro" VARCHAR,
    "catalog" INTEGER,
    "cover" VARCHAR,
    "status" VARCHAR,
    "tags" VARCHAR,
    "isOnline" BOOL
);