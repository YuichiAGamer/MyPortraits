package com.cosmicbarrage;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * このパッケージ内のクラスにおけるDrawEventを統括するクラス。
 * この中のListに、各クラスに実装されたDrawEventListenerのonDrawEventを登録し、
 * 統括管理する。
 * */
public class DrawEventManager {

	//登録されたイベントのリスト
	private List<DrawEventListenerWithOrder> eventList = null;
	
	/**
	 * コンストラクタ
	 * */
	public DrawEventManager(){
		eventList = new ArrayList<DrawEventListenerWithOrder>();
	}
	
	
	/**
	 * 指定されたDrawEventListenerを、指定されたDrawingOrderで追加するメソッド。
	 * なお、DrawingOrderを宣言しない場合、
	 * オーバーライドされたメソッドにより、MIDDLE列挙子が自動選択される
	 * */
	public void addDrawEvent(DrawEventListener namedEvent){
		//列挙子の自動選択
		DrawingOrder drawingOrder;
		drawingOrder = DrawingOrder.MIDDLE;
		
		DrawEventListenerWithOrder wrappedEvent
		= new DrawEventListenerWithOrder(namedEvent, drawingOrder);
		eventList.add(wrappedEvent);
	}
	/**
	 * 指定されたDrawEventListenerを、指定されたDrawingOrderで追加するメソッド。
	 * なお、DrawingOrderを宣言しない場合、
	 * オーバーライドされたメソッドにより、MIDDLE列挙子が自動選択される
	 * */
	public void addDrawEvent(DrawEventListener namedEvent, DrawingOrder drawingOrder){
		DrawEventListenerWithOrder wrappedEvent
		= new DrawEventListenerWithOrder(namedEvent, drawingOrder);
		eventList.add(wrappedEvent);
	}
	
	/**
	 * 指定されたDrawEventListenerを、そのオーダーごとこのクラスのリストから削除するメソッド
	 * */
	void removeDrawEvent(DrawEventListener namedEvent){
		//eventListはDrawEventListenerをラップしたインスタンスなので、
		//getメソッドとfor節のコンボで総当たりをして、指定された名前のリスナーを捜索する
		for (int i = 0; i < eventList.size(); i ++){
			if (eventList.get(i).event.equals(namedEvent) == true) {
				eventList.remove(i);
			}//if節終了		
			
		}//for節終了		
		
	}//removeDrawEventメソッド終了
	
	/**
	 * 現時点でこのクラスに登録されたDrawEventListenerのイベントを一斉起爆させるメソッド。
	 * なお、イベントを起爆する際、イベントは3段階に分けて行われる。
	 * 最初はDrawingOrder=FIRSTであるイベント、次はMIDDLEであるイベント、
	 * そして最後にLASTであるイベント……と、順次行われていく
	 * */
	public void fireDrawEvent(Image targetImage){
		//もしeventListがnullなら、その時点で強制リターン
		if (eventList == null){
			return;
		}
		
		//強制リターンがかからないなら、for節を使って、現在登録されているイベントを順次起爆
		
		//まずはFIRSTのイベント
		for (int i = 0; i < eventList.size(); i++){
			if (eventList.get(i).order == DrawingOrder.FIRST){
				eventList.get(i).event.onDrawEvent(targetImage);
			}			
		}//FIRSTのfor節終了
		
		//次にMIDDLEのイベント
		for (int i = 0; i < eventList.size(); i++){
			if (eventList.get(i).order == DrawingOrder.MIDDLE){
				eventList.get(i).event.onDrawEvent(targetImage);
			}			
		}//MIDDLEのfor節終了
		
		//最後にLASTのイベント
		for (int i = 0; i < eventList.size(); i++){
			if (eventList.get(i).order == DrawingOrder.LAST){
				eventList.get(i).event.onDrawEvent(targetImage);
			}			
		}//LASTのfor節終了	
	}//fireDrawEventメソッド終了
	
	/**
	 * DrawEventListenerに、DrawingOrderの情報を外部から追加し、運用する目的で作成された内部クラス。
	 * DrawEventManagerクラス内で作成されるリストは、実質上このクラスのインスタンスのリスナーである
	 * */
	private class DrawEventListenerWithOrder{
		
		//メンバ変数(フィールド)
		DrawEventListener event = null;
		DrawingOrder order = null;
		
		//コンストラクタ
		public DrawEventListenerWithOrder(DrawEventListener namedEvent,
				DrawingOrder drawingOrder) {
			// TODO Auto-generated constructor stub
			event = namedEvent;
			order = drawingOrder;
		}
		
	}//内部クラス終了
	
	/**
	 * DrawEventManagerにおいて使う列挙型。
	 * 現在リストに登録されているインターフェイスのメソッドを、どのタイミングで実行するか定める
	 * */
	public enum DrawingOrder{
		/**最初に処理*/
		FIRST,
		/**FIRSTの処理後に処理*/
		MIDDLE,
		/**最後に処理*/
		LAST		
	}//列挙型終了
}