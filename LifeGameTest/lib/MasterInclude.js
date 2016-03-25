/*
 JavaScript で、いわゆるC言語のインクルードや、
 Java のパッケージインポートのような真似をしたい場合、
 方法はいくつかあるが、代表的なものは、 .js ファイルの頭に次のようなタグを加え、
 タグの中味にの指すファイルの内容を、ここに展開すること。

 document.write('<script type="text/javascript" language="JavaScript" src="..hoge/fuga.js"></script>');

 この際、srcに指定するのは、最終的にこのJavaScriptを実行するhtmlファイルから見た、
 呼び出し対象の .jsファイルの相対パス。*このファイル自身から見た相対パスではない*。
 
 なお、このややトリッキーな呼び出しが、ライブラリの肥大化とともに保守性を下げるリスクも想定されるが、
 以下のような回避方法があるらしい。ちょっとテクニカルだけど、場合によっては試してみよう。
 
 http://d.hatena.ne.jp/oinusama/20100204/p1
*/

document.write
('<script type="text/javascript" language="JavaScript" src="./lib/CellGrid/CellGrid.js"></script>');