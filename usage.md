##### この内容は、zipに同梱されているreadme.txtとほぼ同じ内容です。

注意
--

+ 本ツールを利用してスキルを登録する際には必ず現在のスキル帳をいったんエクスポートするようにしてください。
+ 本ツールを使用した結果生じたいかなる損害も作者は責任を負いません。予めご了承ください。
+ 本ツールのソースコードは大部分がXGCrawlerを参考にしています。本ツールがXGCrawlerに対して何らかの不利益を発生させていることが分かった場合、本ツールは予告なく配布を終了することがあります。

概要
--

eAMUSEMENTサイトからGITADORA Tri-boostのプレイデータを読み込み、[GITADORA Tri-boost スキルシミュレータ](http://tri.gfdm-skill.net)にインポート可能なCSVファイルを出力するツールです。

特徴
--

+ [XGCrawler](http://suitap.iceextra.org/xgcrawler/)と同じ環境で実行できます。前作OverDriveまでこちらを利用していた方は同じ設定ファイルを使いまわせます。

内容物
--

+ config.xml(設定を記述するファイル)
+ GDSkillGetter.jar(ツール本体)
+ README.txt(使い方などが書かれた説明書)
+ run.bat(Windows実行用バッチファイル)

使い方
--

1. DLしたzipファイルを適当な場所に展開
1. zipに同梱されているconfig.xmlを適当なテキストエディタで開く(Windows付属のメモ帳でOK)
1. 以下の情報を適切な場所に入力
    + EAGateUsername: eAMUSEMENTのログインID
    + EAGatePassword: eAMUSEMENTのパスワード
1. 実行する
    + Windowsの場合: 同梱してあるrun.batをダブルクリック
    + Mac / Linuxの場合: ターミナルで本体があるディレクトリまで移動し、```java -jar GDSkillGetter.jar```と入力
1. 黒いウィンドウが開くので数分から十分ほど待つ
1. 同フォルダに「output.csv」が生成されたら終了
1. (手動でTri-boostスキルシミュの上部メニューからoutput.csvをインポートしてください)

※  2回目以降は、設定値に変更がなければ4.から行えば大丈夫です

※ 設定ファイルの詳しい設定方法については、[XGCrawlerの使い方](http://suitap.iceextra.org/xgcrawler/#howtouse)をご覧ください

おかしいな？と思ったら
--

###### 作者Twitterは[@ENIXERvsREXINE](https://twitter.com/ENIXERvsREXINE)です。もしいろいろ試してみてもわからなかった場合や不具合などを見つけた場合にはご連絡ください。

+ 「設定ファイルが見つかりません」というエラーが出る
    + 設定ファイルの名前が変更されてしまっている可能性があります。GDSkillGetter.jarと同じディレクトリ(フォルダ)に、「config」という名前を(全て小文字で)含んだ、拡張子が「.xml」のファイルを1つだけ置いてください。
+ 「予期しない設定ファイル名です」というエラーが出る
    + 通常起こりえないエラーです。もし発生した場合は、設定ファイルのなるべく詳しい情報を添えて作者までご連絡ください。
+ 「eAMUSEMENT にログインできませんでした」というエラーが出る
    + eAMUSEMENT のユーザー名もしくはパスワードが誤っています。config.xml の EAGateUsername 欄と EAGatePassword 欄を再度確認してください。
+ run.bat を実行したけど「指定されたファイルが見つかりません」というエラーが出る
    + Javaがインストールされていないかもしれません。Javaがインストール済みである場合、run.batの9行目の```XGCrawler.jar```と書かれている行を、```java -jar XGCrawler.jar```もしくは```C:\Windows\System32\java.exe -jar XGCrawler.jar```もしくは```"C:\Program Files\Java\jre7\bin\java.exe" -jar XGCrawler.jar```と書き換えて実行してみてください(コピー＆ペーストで貼り付けると楽です)。ファイルの編集は run.bat を右クリック→「編集」で出来ます(メモ帳が開きます)。
+ 「プレーデータが正常に読み込めません」といったメッセージが出る
    + プレーデータのページから正しく達成率データが読み込みできなかった時に表示されます。正しく読み込みできなかった曲については更新処理をせずに次の曲の処理を続けるようになっていますので、一旦実行が終わった後にもう一度実行すればOKです。
    + このメッセージは、「GDSkillGetterを実行しつつプレーデータをブラウザもしくはGITADORAアプリで見ている」場合に必ず出てきます。XGCrawler実行中は、プレーデータを参照しないようにしてください。

今後の予定
--

+ スキルシミュへの投稿の自動化
+ 高速化
+ すまほ・・・？
+ GUI・・・？
+ 他機種対応・・・？

・更新履歴
--

|日付|ver|内容|
|---|---|---| 
|8/28   |0.1.0|UpdateOnlySkillTarget, AddSkillInfoToCommentsに対応|
|8/12   |0.0.1-beta2|作者の設定ファイルが誤ってツール本体に混入していた問題を修正|
|8/9|0.0.1-beta)|配布開始|
