using UnityEngine;
using System.Collections;

/// <summary>
/// カメラとプレイヤーの相対距離を一定に保つためのスクリプト。
/// プレイヤーオブジェクトの子オブジェクトとしてカメラを配置することもできるが、
/// そうするとプレイヤーオブジェクトがスピンした際、画面もスピンしてしまい見辛い。
/// 原則として、カメラをプレイヤーの子オブジェクトにするのはバッドノウハウと覚えておこう。
/// </summary>
public class CameraController : MonoBehaviour {

    // プレイヤーオブジェクトへの参照
    public GameObject playerReference;
    private Vector3 offset;

	// Use this for initialization
	void Start () {

        // 初期オフセットを記録
        offset = transform.position - playerReference.transform.position;

	}
	
	/// <summary>
    /// LateUpdate は、他のオブジェクトの Update () 実行がすべて終わった後で実行されるメソッド。
    /// カメラの位置移動は、他のオブジェクトの、そのフレームにおけるレンダリングが終了して、
    /// いわゆる「お化粧が完了」した状態での行った方が自然なので、
    /// LateUpdate () によるカメラの位置移動はセオリーと覚えておこう。
    /// </summary>
	void LateUpdate () {
        // フレーム更新のたびに、カメラの位置を修正
        transform.position = playerReference.transform.position + offset;
	}
}
