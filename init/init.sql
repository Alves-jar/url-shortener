IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'url_shortener')
BEGIN
    CREATE DATABASE url_shortener;
END
GO

USE url_shortener;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='urls' AND xtype='U')
BEGIN
    CREATE TABLE urls (
        id            BIGINT IDENTITY(1,1) PRIMARY KEY,
        shorten_code  VARCHAR(20)      NOT NULL UNIQUE,
        original_url  NVARCHAR(2000)   NOT NULL,
        clicks       BIGINT           NOT NULL DEFAULT 0,
        createdAt  DATETIME2        NOT NULL DEFAULT GETDATE(),
        expireAt DATETIME2       NULL
    );

    CREATE INDEX idx_shorten_code ON urls(shorten_code);
END
GO