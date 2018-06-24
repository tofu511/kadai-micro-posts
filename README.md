# micro-posts

## 概要
ScalaとPlayフレームワークで作ったTwitterクローンです。

## バージョン
- Play: 2.5.14
- Scala:2.11.11
- sbt: 0.13.15
- MySQL: 5.7.20

## 動かす上での前提条件
- sbtがインストールされていること。
- MySQLがインストールされていること。
- `/create-local-mysql-db.sh`を実行。もしくは`/create-mysql-db.sql`の内容に従ってデータベースを作成すること。
- `env/dev.conf`の`jdbcPassword`を適切な値に修正すること。

## 実行方法
```bash
sbt flywayMigrate
sbt run
```

```bash
# sbt flywayMigrateが失敗してやり直す場合
sbt flywayClean
sbt flywayMigrate
sbt run
```


