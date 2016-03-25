/*
html5 + JavaScript でライフゲームを再現するためのライブラリ。
1 セルの大きさを 16 px  × 16 px として、canvas の幅 ÷ 16 、高さ ÷ 16 で、
セル数を決める。
それ以外は通常のライフゲームと同様の実装。なお、初期セルの生死はランダムで、
生 = true 、 死 = false で表す。
*/

/* 各セルの生死を記録する配列 */
var cells = new Array();

/*
Canvas 上に存在するセル数。cellsXは横方向のセル数、 cellsY は縦方向のセル数
*/

/**
Canvas の高さと幅から、このcanvas 上にいくつセルを作成できるか計算後、
対応する長さの二次元配列を作成し、かつその配列の状態をランダムに決定する。
*/
function createCells() {
		
		var cellsX =Math.floor(document.getElementById("canvas1").width / 16);
		var cellsY =Math.floor(document.getElementById("canvas1").height / 16);
		
		// 二次元配列作成
		for (var i = 0; i < cellsX; i++) {
			cells[i] = new Array(cellsY);			
		}
		
		// 初期値ランダマイズ
		for (var i = 0; i < cellsX; i++) {
			for (var j = 0; j < cellsY; j++) {
				var rand = Math.random() * 2;
				if (rand >= 1){
					cells[i][j] = true;
				} else {
					cells[i][j] = false;
				}				
			}
		}
		
//		console.log("cellsX is :" + (cellsX)); // 30
//		console.log("cellsY is :" + (cellsY)); // 20
//		console.log("cells[0][0] is :" + (cells[0][0]));
//		console.log("cells[cellsX - 1][cellsY - 1] is :" + (cells[cellsX -1][cellsY - 1]));
//		console.log("cells.length is :" + (cells.length)); // 30
//		console.log("cells[0].length is :" + (cells[0].length)); // 20
	
	}

/*
作成されたセルについて、各々生死を判定し、判定結果に応じて生死をそれぞれ設定する
*/
function updateAllCells() {
	
	// 次世代のセルの生死一覧表。全セルの生死判定を行い、
	// 最後に現行のセルをまとめてこの配列と入れ替える
	var nextCells = new Array(cells.length);
	
	for (var i = 0; i < nextCells.length; i++) {
			nextCells[i] = new Array(cells[i].length);			
		}
	
	// 現行のセルを参照しながら、上記セルの生死判定を実行
	for (var i = 0; i < cells.length; i++) {
		for (var j = 0; j < cells[i].length; j++) {
			nextCells[i][j] = checkEachCellsNextState(i, j);
		}
	}
	
	// 生死判定完了後、現行のセルと入れ替え
	cells = nextCells;
	
}

/* 
指定された座標のセルについて、次世代の生死を判定し、
その結果を true/false で返す
 */
function checkEachCellsNextState(i, j) {
	
	// 指定されたセルの周囲 8 マスに、生きているセルがいくつか確認。
	// ただし、参照する先のセルの添え字が 0 未満か、セル配列の長さ以上になるなら、
	// その際の判定はスキップ
	var aliveCellsNumber = 0;
	
	for (var x = i - 1; x <= (i + 1); x++) {
		
		// x 座標がはみ出たならスキップ
		if ((x < 0) || (x >= cells.length)) {
			continue;
		}
		
		for (var y = j - 1; y <= (j + 1); y++) {
			// y 座標がはみ出たならスキップ
			if ((y < 0) || (y >= cells[x].length)) {
				continue;
			}
			
			// また、自分自身を参照している場合も、カウントから外す
			if (x == i && y == j ) {
				continue;
			}
			
			// 処理がここまで達したなら、現在見ているセルの生死を参照し、
			// aliveCellsNumber を加算
			if (cells[x][y] == true) {
				aliveCellsNumber++;
			}
		}
	}
	
	// これで計算できた周囲のセルの生存数、および自身の今の生死状態をもとにして、
	// 返すべき状態を決定。	
	// 自身が生存している場合、隣接生存セル数 1 以下か 4 以上で死滅。
	// そうでないなら引き続き生存
	if (cells[i][j] == true) {
		
		if ((aliveCellsNumber <= 1) || (aliveCellsNumber >=4)) {
			return false; // 過密or過疎で死亡
		} else {
			return true; // 生存
		}
	} else { // 自身が現在死亡状態である場合
		if (aliveCellsNumber == 3) {
			return true; // 新たに生命誕生
		} else {
			return false; // 誕生せず
		}
	} 	
}

var intervalMillisec_drawCells = 0;

/*
Canvas に現在のセルの生死状態を描画させるメソッド。
1 個目の変数は、前回のブラウザのフレーム更新から経過したミリ秒数。
この値が累積し、 1000 を越えた時点でメソッド本体を実行し、更新を行う。
これにより、セルの 1 秒ごとの世代交代を実現。
なお、 2 個目の変数に true を入れた場合は、この 1000 秒というインターバルを無視して、
強制的に現在のセルの静止状態を再描画できる(この際、 1 個目の引数 interval の累積も発生しない)
*/
function drawCells(interval, ignoreInterval) {
	
	if (ignoreInterval == false) {
		intervalMillisec_drawCells = intervalMillisec_drawCells + interval;
		
		if (intervalMillisec_drawCells < 1000) {
			return;
		} else {
			intervalMillisec_drawCells = 0;
		}
		
		// セルの生死判定実行
		updateAllCells();
	}
	
	var myCanvas = document.getElementById("canvas1");
		
	// 現在のキャンバスの幅と高さを取得
	// (".style.width"という宣言方式だと、"(数字)"ではなく"(数字)px"という
	// 形式の文字列が取れてしまうので注意)
	var width = myCanvas.width;
	var height = myCanvas.height;
			
	var ctx = document.getElementById("canvas1").getContext("2d");
	ctx.fillStyle = "rgba(0, 0, 0, 1)";
	ctx.fillRect(0, 0, width, height);
	ctx.save();
			
	ctx.fillStyle = "rgba(64, 255, 64, 1)";
	
	// 現在のセルの生死判定をもとに、生存状態のセルに色づけ
	for (var i = 0; i < cells.length; i++) {
		for (var j = 0; j < cells[i].length; j++) {	
			if (cells[i][j] == true) {
				ctx.fillRect(i * 16, j * 16, 16, 16);
				ctx.strokeStyle = "rgba(0, 0, 0, 1)";
				ctx.strokeRect(i * 16, j * 16, 16, 16);
			} 			
		}
	}
	
}

/*
この Canvas のクリックされた座標を読み取り、
その座標に対応したセルを、復活させるメソッド。
なお、クリックしたセルのみ復活させると、セルがたちまち「孤独死」して
レスポンスを得られない危険性があるため、

*/
function reviveCell(e) {
	
	console.log("reviveCell() is starting");
	
	var x = 0;
	var y = 0;
	
	// まず、最初はクリック座標の補正から。
	// この場合、クリックされた座標について、実際に欲しいのは Canvas 内の
	// 相対座標だが、 window.e として届くイベント情報には、ブラウザの絶対座標が格納されている。
	// よって、この canvas がブラウザ内のどの座標を基準としているかをまず求める。
	var canvas = document.getElementById("canvas1");
	var canvasRect = canvas.getBoundingClientRect();
	
	x = e.clientX - canvasRect.left;
	y = e.clientY - canvasRect.top;
	
	console.log("x is :" + x);
	console.log("y is :" + y);
	
	var cellX = Math.floor(x / 16);
	var cellY = Math.floor(y / 16);
	
	if ((cellX >= cells.length - 1) && (cellY >= cells[cellX].length - 1)) {
		cells[cellX][cellY] = true; 
	} else if (cellX >= cells.length - 1) {
		cells[cellX][cellY] = true;
		cells[cellX][cellY + 1] = true;
	} else if (cellY >= cells[cellX].length - 1) {
		cells[cellX][cellY] = true;
		cells[cellX + 1][cellY] = true;
	} else {
		cells[cellX][cellY] = true;
		cells[cellX + 1][cellY] = true;
		cells[cellX][cellY + 1] = true;
		cells[cellX + 1][cellY + 1] = true;
	}
	
	// セル復活の結果を強制再描画
	drawCells(0, true);
	
}







