package com.cosmicbarrage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Cosmic Barrage　において、敵キャラ全般のデータと動作を管理するクラス。<br>
 * 実用時には、敵一体につきこのクラスのインスタンスを一つずつ宣言し、<br>
 * それらを親クラスのコレクションに格納して運用すること。<br><br>
 * <b>リファクタリング(2012/12/17)</b><br>
 * このクラスは、ArrayListを継承したEnemyHiveクラスにラップして使う方式に<br>
 * リファクタリング。ImageIndexChangerスレッドは、EnemyHiveスレッドに移植。<br>
 * このEnemyクラスが発行するスレッドは、破壊時のBlastImageThreadのみに変更。
 * @author Yuichi Watanabe
 * @since 2012/12/10
 * @version 1.1.0 Code Refactored on 2012/12/17
 * */
public class Enemy{

	/* *
	 * フィールド(メンバ変数)
	 * */
	
	//自身の持つ個体識別用ID
	int myID;
	
	//親フォームへの参照
	private CosmicBarrage parentFrame;
	
	//この敵のグラフィック
	BufferedImage enemyImage;
	
	//この敵が爆散するグラフィック
	BufferedImage blastImage;
	
	//その瞬間において、敵グラフィックの何枚目を表示するかを示すインデックス。
	//0~3のいずれかの値をとる
	int imageIndex = 0;
	
	//imageIndexの値に応じて決まる、enemyImageの表示領域
	Rectangle displayingRegion = new Rectangle(0, 0, 32, 32);
	
	//imageIndexChangerはEnemyHiveに移植したので、メンバ型も削除
	
	//幅、高さ、x、y、座標四点セット
	int width;// = 32;
	int height;// = 32;
	int x;// = 0;
	int y;// = 0;
	
	//当たり判定が実表示より何ピクセル小さいかを示すオフセット。
	//デフォルト値は「2」を指定。
	//なお、"Hit Checking"は「当たり判定」の英訳として採用したターム
	int hitCheckingOffset = 2;
	
	//この敵が「生きて」いるかどうかを現すフラグ。このフラグがtrueである間、
	//敵グラフィックの描画は続く。また、これがfalseである場合、当たり判定も消滅
	boolean isAlive = true;
	
	//スコア表示を実装するので、digitManagerクラスの型も宣言
	DigitManager digitManager;
	
	/**
	 * コンストラクタ
	 * */
	Enemy(CosmicBarrage parentFrameInput, int x, int y, Integer graphicID, int namedID){
		//親クラスの参照ゲット
		parentFrame = parentFrameInput;
		
		//自身のIDを確定
		this.myID = namedID;
		
		//各種メンバ変数も、ここで明示的に値を与える
		width = 32;
		height = 32;
		this.x = x;
		this.y = y;
		hitCheckingOffset = 1;
		digitManager = new DigitManager();
		
		
		//引数graphicIDから、敵グラフィックのリソースにアクセスする文字列を作成
		String resourcePath = "Enemy" + graphicID.toString() + ".png";
		
		//上で作成した文字列をアクセスキーにして、敵イメージをリソースからゲット
		enemyImage = null;		
		try {
			enemyImage = ImageIO.read(getClass().getResource(resourcePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//imageIndexChangerは、EnemyHiveクラスに移植
		
		
	}//コンストラクタ終了
	
	/*
	 * 以下、メソッドおよび内部クラスを列挙する。
	 * 列挙する順番は
	 * 1.インターフェイスにより実装されたメソッド
	 * 2.通常のメソッド
	 * 3.内部クラス
	 * 4.デストラクタメソッド
	 * の順とする。
	 * 1,2,3のグループ内では、それぞれ更にABC順でメンバが並んでいる。
	 * オーバーロードされたメソッドは、引数が少ない順から多い順に並べる
	 * */
	
	/* ##############################
	 * 1.インターフェイスにより実装されたメソッド
	 * ##############################*/	
	
	//敵イメージの描画はEnemyHiveクラスに一任したので、
	//EnemyHiveクラスにコードを移植済み
	
	/* ############
	 * 2.通常のメソッド
	 * ############*/

	/**
	 * このenemyインスタンスが破壊されたときに起動されるメソッド。
	 * このメソッドが起動した瞬間に、このクラスのisAliveはfalseとなり、
	 * 通常のグラフィックではなく爆発のエフェクトが描画される。
	 * この爆発のエフェクトは30フレーム続き、最後の1フレームが描画され終えた瞬間、
	 * インスタンスはenemyListから外され、自身も破棄される
	 * */
	public void breakDown(){
		
		//isAliveをfalseにセット
		isAlive = false;
		
		//爆発イメージの描画を別スレッドで開始。
		//なお、このメソッドの処理内容は他の内部クラスに切り出す
		Thread blastImageThread = new BlastImageThread();
		blastImageThread.start();
		
	}//breakDownメソッド終了
	
	/**この敵の中心座標を返却するメソッド*/
	public Point getCenterLocation(){
		int centerX = this.x + this.width;
		int centerY = this.y + this.height;
		Point point = new Point(centerX, centerY);
		
		return point;
	}
	
	/* ##########
	 * 3.内部クラス
	 * ##########*/
	
	/**
	 * 爆発イメージの描画を担当する処理クラス。
	 * なお、このクラスの処理が最後まで進んだなら、
	 * インスタンスのデストラクタが起動して、インスタンスが丸ごと破棄される
	 * */
	class BlastImageThread extends Thread{
		
		//爆心地座標
		private List<Point> blastPoints = new ArrayList<Point>();
		//爆風半径。なお、Listのジェネリックには基本型の宣言はできないので注意
		private List<Integer> blastRadiuses = new ArrayList<Integer>();
		//乱数
		private Random random = new Random();		
		
		@Override
		public void run(){
			
			//30フレーム間、処理を繰り返す
			for(int frameNumber = 1; frameNumber <= 30; frameNumber++) {
				
				//blastImageを初期化
				blastImage = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
				Graphics g = blastImage.getGraphics();
				
				/*次に、爆発を描画。爆発のグラフィックのアルゴリズムは以下の通り。
				 * 1.爆発のグラフィックは、1フレームごとに半径が1ずつ大きくなる白い円である
				 * 2.敵の爆発エフェクトにかける時間は30フレーム。
				 * 爆発一つにかけるフレーム数は8フレームとする。つまり、爆風一つが消える寸前は、
				 * 爆風の半径は8ピクセル
				 * 3.爆発が始まった最初の1フレーム目、及び最終フレームで爆発をぴったり終えられる
				 * 22フレーム目、この2フレームでは必ず爆発を発生させる
				 * 4.また、22フレーム目までの各フレームにおいても、新しい爆風が1/4の確率で
				 * 発生するものとする
				 * 5.いずれのフレームにおいても、爆風が発生する場合、(8, 8)~(40, 40)のいずれかの
				 * 座標がランダムに選ばれ、そこが爆心地となる*/
				
				/* ###############
				 * 爆風の情報処理編
				 * ###############*/
				
				//まずはListに含まれている爆風の半径をそれぞれすべて+1
				for (int i = 0; i < blastRadiuses.size(); i++) {
					blastRadiuses.set(i, blastRadiuses.get(i) + 1);
				}
				
				//この上で、この状態のListの中から、blastRadiusesが8を越えているものがあった場合、
				//そのインデックスを持つ情報を削除
				for (int i = 0; i < blastRadiuses.size(); i++){
					if (blastRadiuses.get(i).intValue() > 8) {
						blastPoints.remove(i);
						blastRadiuses.remove(i);
					}				
				}
				
				/*そして、現在のフレームで、新たな爆発点を設定するかどうか決める。
				1フレーム目と22フレーム目は必ず爆風を発生させ、23フレーム目以降は爆風を発生させない。
				2~21フレーム目では、1/4の確率でランダムに爆風が発生*/
				if ( (frameNumber == 1) || (frameNumber == 22) ) {
					//座標をランダムに決定
					int x = random.nextInt(33) + 8;
					int y = random.nextInt(33) + 8;
					
					//この座標データを、Listに追加
					blastPoints.add(new Point(x, y));
					blastRadiuses.add(1);
				}else if ((frameNumber >= 2) && (frameNumber <= 21) &&
						  (random.nextInt(4) == 0) ) {
					//座標をランダムに決定
					int x = random.nextInt(33) + 8;
					int y = random.nextInt(33) + 8;
					
					//この座標データを、Listに追加
					blastPoints.add(new Point(x, y));
					blastRadiuses.add(1);
				}
				
				/* ###########
				 * 爆風の描画編
				 * ###########*/
				
				//上記の爆風の情報処理を終えたなら、現在Listに格納されている爆風の
				//情報を元に、爆風を描画
				
				for(int i = 0; i < blastPoints.size(); i++) {
					//爆風を現す円の描画起点を、爆風の発生座標-爆風半径で計算
					int x = blastPoints.get(i).x - blastRadiuses.get(i);
					int y = blastPoints.get(i).y - blastRadiuses.get(i);
					
					//爆風は、まず白い円を描画して、その周囲を黒い線で縁取ることで描画される
					g.setColor(Color.WHITE);
					g.fillOval(x, y, blastRadiuses.get(i) * 2, blastRadiuses.get(i) * 2);
					g.setColor(Color.BLACK);
					g.drawOval(x, y, blastRadiuses.get(i) * 2, blastRadiuses.get(i) * 2);
					
				}
				
				//試しに、スコアも表示してみる
				/*BufferedImage scoreImage = digitManager.getDigitGraphic(100, DigitFontOption.HARD);
				//このフレームでスコアを表示するべき始点座標を計算
				int scoreX = (48 - scoreImage.getWidth()) / 2;
				int scoreY = (48 - scoreImage.getHeight()) - frameNumber;
				
				g.drawImage(scoreImage,
							scoreX, scoreY,
							scoreX + scoreImage.getWidth() * 1, scoreY + scoreImage.getWidth() * 1, 
							0, 0, scoreImage.getWidth(), scoreImage.getWidth(),
							parentFrame);*/
				
				//最後に、1フレームぶんだけスリープ
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}//for節終了
			
			//爆風の描画が終わったなら、blastImageをNullにした上で
			//デストラクタを起動
			blastImage = null;
			destructEnemy();
		}
	}

	/* ###############
	 * 4.デストラクタメソッド
	 * ###############*/
	
	/**
	 * このインスタンス自身を破壊するメソッド。<br>
	 * 具体的に行われる処理は以下の通り。<br><br>
	 * 1.自身のisAliveフラグをfalseにセット<br>
	 * 2.親クラスのenemyListから、自身の登録をremoveする<br>
	 * 3.親クラスのdrawEventManagerから、自身の登録を外す<br>
	 * 4.同時に自身の参照をnullにする<br>
	 * 5.最後にGCを手動起動し、完全にインスタンスを消滅させる
	 * */
	void destructEnemy() {
		
		//isAliveフラグをfalseにする
		isAlive = false;
		
		//親クラスのenemyListから、自身を探し出す。この際IDをキーとする。
		//自身を表すenemyListの要素を見つけたら、それを破棄する。
		
		//まずはIDから自身の参照を確定
		Enemy myself = null;
		for (int i = 0; i < parentFrame.enemyHive.size(); i++) {
			if (parentFrame.enemyHive.get(i).myID == myID) {
				myself = parentFrame.enemyHive.get(i);
				break;//参照が確定したならそれ以上続けなくてよいのでbreak
			}
		}
		
		//myselfをenemyList上から破棄
		parentFrame.enemyHive.remove(myself);
		
		//自身への参照をnullにする
		myself = null;
		
		//最後に、GCを手動起動
		System.gc();
	}
	
	
	
}//Enemyクラス終了

/**
 * 余談ながら、あるクラス内の処理の都合でThreadクラスの内部クラスや無名クラスを宣言した際、参照に注意。
 * スレッドを発行したクラス内で、無名クラスや内部クラスを作り、その中で"this"トークンを使うと、
 * その"this"が参照するのは、スレッドを発行したクラスではなく、無名クラスや内部クラスそのもの。
 * そのため、スレッド発行元クラスの変数やオブジェクトを参照する場合、"this"を使わず、
 * 直接変数やオブジェクトの名前を書き込むこと。
 * 
 * 発行元クラスと無名/内部クラスで似通った名前の変数を使っており、
 * 発行元クラスのメンバ名と無名/内部クラスのメンバ名が紛らわしい……などの理由で、
 * どうしても"this"相当の参照トークンを使いたい場合は、
 * スレッド発行元のクラスの参照を明示的に渡すという解決策がある。
 * Threadクラスを継承した内部クラスをインスタンス化させる際、
 * その内部クラスに発行元クラスの参照をラップする形で、型宣言を行う。
 * (例えば"ParentClass parentClassType = parentClassReference;"という参照型を宣言するなど)
 * その型宣言ののち、変数をrunメソッド内で使えば、明示的にスレッドの発行元クラスの参照を行うことができる。
 * (例:parentClassType.parentClassMember,parentClassType.parentClassMethod()など)
 * 
 * ただしこの方法は若干労力がかかり、わずかながら可読性も下がる。
 * また、型宣言をラップしなければいけないという事情も考えると、無名クラスとは相性が悪い解決策である。
 * （無名クラスは通常、内部クラスに切り出すほどではない大きさの処理を書くためのコーディング技術である。
 * つまり、型宣言をラップしなければいけないほど規模の大きい処理が必要なら、
 * そもそもそれは無名クラスの出る幕ではない）
 * 
 * Threadクラスを継承した内部クラスが大規模になるので、
 * スレッド発行元のクラスのソースコードと分割し、別のクラスのソースコードとして記述をした方が、
 * 保守性・可読性が高くなる……
 * などの事情がない限り、わざわざ親クラスの参照の型宣言を明示的に行うくらいなら、
 * 内部クラスの変数名やオブジェクト名を工夫した方が、現実的かつ簡便な方策である。
 * 
 * */

/**
 * Javaのクラスのメンバ初期化についてTips。
 * Javaにおいて、クラスのメンバ変数の初期値設定や、メンバオブジェクトの初期インスタンス化は、
 * 次の二つのどちらか、もしくは両方の方法で行うことができる。
 * 
 * 1.メンバ変数/オブジェクトの型宣言を行う際、同時に宣言を行う
 * 2.そのクラスのコンストラクタで初期値を与える/インスタンス化を行う
 * 
 * この内、1の方法を取った場合は、コンストラクタを介さずに初期値設定/インスタンス化が可能である。
 * ただしこの場合、初期値設定/インスタンス化が行われるタイミングは不確定である。
 * 具体的タイミングを挙げて議論をするならば、
 * このクラスのインスタンスにアクセスが行われ、その変数やオブジェクトの型が必要になったその段階で、
 * メンバ変数宣言時に宣言された通りの初期値宣言/インスタンス化が行われる。
 * シングルスレッドのプログラムでなら、この不確定なタイミングでの初期値宣言/インスタンス化は、
 * 大きな問題にはなりにくいと考えられる。
 * ただし、このCosmicBarrageのようにマルチスレッドでの動作を前提とすると、
 * 1の方法による「逐次的初期化/インスタンス化」は危険である。
 * 何故なら、複数のスレッドがこのクラスのメンバにアクセスすることにより、
 * メンバ変数の非同時的初期化/インスタンス化が起こり、これらのメンバ変数/オブジェクト間での
 * 整合性が失われるリスクがあるためである。
 * 
 * そのため、マルチスレッドプログラミングを行う場合や、
 * その変数の値が明示的に宣言されていないと致命的な不都合が発生する場合は、
 * メンバの初期化/インスタンス化は、可能な限り明示的にコンストラクタ内で行うべきであると考えられる。
 * 
 * ただし変数の初期化をコンストラクタに全て持っていくと、
 * それはそれでコンストラクタが肥大化するという問題が出てくる。
 * もともとのコンストラクタが大きいので、メンバ初期化/インスタンス化が難しい場合、
 * それ自体を別メソッドに切り出すなどの手間がいる場合も想定できる。
 * メンバ変数の「逐次的初期化/インスタンス化」により見越される不都合と、
 * コンストラクタの肥大化によるコードの可読性の低下は、両天秤にかけつつ判断を行うこと。
 * */
