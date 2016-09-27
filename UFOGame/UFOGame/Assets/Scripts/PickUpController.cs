using UnityEngine;
using System.Collections;

public class PickUpController : MonoBehaviour {

	// Use this for initialization
	void Start () {
        Debug.Log("Hello, world");
    }
	
	// Update is called once per frame
	void Update () {
        Vector3 rotateAngle = new Vector3(0, 0, 120);
        this.transform.Rotate (rotateAngle * Time.deltaTime);
        // deltaTime を使う場合は、「deltaTime で乗算するパラメータが、
        // 毎秒どれくらいの数値で変異してほしいか」という観点から、相手パラメータを設定しよう
	}
}
