# micro-posts

## 概要
ScalaとPlayフレームワークで作ったTwitterクローンです。

## バージョン
- Play: 2.5.14
- Scala:2.11.11
- sbt: 0.13.15
- MySQL: 5.7.20
- Docker: 18.09

## 動かす上での前提条件
- sbtがインストールされていること。
- Dockerがインストールされていること
- `env/dev.conf`の`jdbcPassword`を適切な値に修正すること。

## 実行方法
```bash
cd docker
docker-compose up
sbt flywayMigrate
sbt run
```

```bash
# sbt flywayMigrateが失敗してやり直す場合
sbt flywayClean
sbt flywayMigrate
sbt run
```


