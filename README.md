# VillagerTitles

村人の頭上に日本語で職業名を常時表示する PaperMC 用プラグインです。

## 特徴

* **自動タイトル表示**
  村人の職業に応じたタイトルを常時表示します。ニート／Nitwit も判定します。
* **既存村人のサポート**
  サーバー起動時に存在するすべての村人をスキャンし、タイトルを付与します。
* **リアルタイム更新**
  *スポーン*（自然・繁殖・スポーンエッグ）および *職業ブロック変更* 後に即時（1 tick 後）更新します。
* **軽量 & 依存なし**
  他プラグインへの依存はなく、Paper／Spigot のみで動作します。

## 動作確認環境

| 項目      | 推奨                |
| ------- | ----------------- |
| サーバーソフト | PaperMC 1.21.x    |
| Java    | 17+               |
| Kotlin  | 1.9+ (プラグイン内部で使用) |

## ビルド

```bash
git clone https://github.com/yourname/VillagerTitles.git
cd VillagerTitles
./gradlew shadowJar        # Windows の場合 gradlew.bat shadowJar
```

`build/libs/VillagerTitles-<version>-all.jar` が生成されます。

## インストール

1. 生成／入手した **VillagerTitles.jar** を `plugins/` フォルダーへ配置
2. サーバーを再起動（または `/reload confirm`）
3. コンソールに `VillagerTitlesPlugin enabled` が表示されれば完了

## 設定 / コマンド / 権限

現バージョンでは設定ファイル・コマンド・パーミッションはありません。
プラグインを配置するだけで機能します。

## 開発者向け

* Kotlin + Gradle Kotlin DSL
* Spigot / Paper API 1.21.3
* JDK 17

Issue・PR 大歓迎です！

## ライセンス

[MIT License](LICENSE)
