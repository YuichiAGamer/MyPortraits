package com.cosmicbarrage;

import java.awt.Rectangle;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 現在進行中のゲームにおける得点や経過時間、定期的に発生するイベントなどを管理するクラス。<br>
 * ゲームが始まってからの経過時間もこのクラスで記録する。<br>
 * 2012/12/18現在は未実装だが、画面上に得点を表示しておくコンソール機能なども<br>
 * 追い追い実装して行く予定。
 * @author Yuichi Watanabe
 * @since 2012/12/18
 * @version 1.00
 * */
public class GameMonitor {

	/**
	 * メンバ変数
	 * */
	public long ellapsedTime;//経過時間(ミリ秒単位。長時間の計測を想定しlongを使用)
	public Timer timer;//外部からタイマーを起動するための型宣言
	private CosmicBarrage parentFrame;//親クラスへの参照
	
	/**
	 * コンストラクタ
	 * */
	GameMonitor(CosmicBarrage parentFrameInput){
		//経過時間を0にリセット
		ellapsedTime = 0;
		//timer型にタスクをセット
		timer = new Timer();
		//親クラスの参照をセット
		parentFrame = parentFrameInput;
	}
	
	/**タイムカウント開始用のメソッド*/
	public void startTimer(){
		timer.schedule(new gameTimerTask(), 0, 10);
	}
	
	/**内部クラス*/
	class gameTimerTask extends TimerTask{

		
		//10ミリ秒経過するごとに、ellapsedTimeを10追加する
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ellapsedTime = ellapsedTime + 10;
			
			//経過時間10秒ごとに、新たなボールを生成するイベントを起こす
			if ( (ellapsedTime % 10000 == 0) ) {
				genarateNewBall();
			}
			
		}//runメソッド終了		
		
	}

	/**
	 * 新たなボールをボールリストに追加し、動かすメソッド。<br>
	 * まず敵の存在しない16*16のスペースをランダムに探し出し、その座標で<br>
	 * EffectDrawerクラスの"ballGenerationEffect"を起動してアニメーション。<br>
	 * 最後に、ボールを動かすためのスレッドを起動して終了、という流れになる
	 * */
	public void genarateNewBall() {
		// TODO Auto-generated method stub
		
		//まず、座標をランダムに選別
		Random random = new Random();
		Rectangle myRegion = null;
		boolean isfittingRegion = false;
		
		//敵のいない領域を引き当てるまで、ループを繰り返す
		while(isfittingRegion == false) {
			//ランダム領域はこれ。
			//ボールは生成時、必ず上向きのベクトルを持つようなランダマイズを
			//受けているので、画面下ギリギリで生成されても大丈夫
			int randomX = random.nextInt(640 - 16);
			int randomY = random.nextInt(480 - 16);
			
			//矩形領域を確定
			myRegion 
			= new Rectangle(randomX, randomY, randomX + 16, randomY + 16);
			
			//この領域について、敵の存在/非存在の判定
			isfittingRegion = ! parentFrame.enemyHive.existsAnyEnemies(myRegion);
			
		}
		
		//この領域で、ボールを生成。座標は上記の領域より手動指定
		Ball newBall = new Ball(parentFrame,
								parentFrame.serialID,
								myRegion.x,
								myRegion.y);
		
		parentFrame.ballList.add(newBall);
		
		parentFrame.serialID = parentFrame.serialID + 1;
		
		//そしてballGenerationEffectを起動。
		//なお、こちらのメソッドからは、effectDrawerのメソッドに対してjoinメソッドは
		//使えないので、このballの参照を向こうのメソッドに渡し、
		//そちらで起動してもらう
		parentFrame.effectDrawer.ballGenerationEffect(newBall);		
		
	}
	
}
