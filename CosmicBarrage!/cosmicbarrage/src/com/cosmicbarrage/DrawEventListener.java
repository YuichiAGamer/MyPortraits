package com.cosmicbarrage;

import java.awt.Image;

/**
 * このゲーム内で、画像データを持つクラスが実装するイベントリスナー。
 * このイベントリスナーでは、"DrawEventManager"クラスのfireDrawEventが起きた時、
 * 各クラスのインスタンスが実行するonDrawEventを定義している。
 * この中に、親クラスのバッファリングイメージに何らかの画像を描き込む処理を記述すること*/
public interface DrawEventListener {

	void onDrawEvent(Image targetImage);
}
