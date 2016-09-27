using UnityEngine;
using System.Collections;
using UnityEngine.UI; // テキスト UI を参照するため

public class PlayerController : MonoBehaviour {

    // 自身の "Rigidbody2D" への参照ホルダー。
    // ひとまずこれは、 "PlayerController" オブジェクトにセットされた、
    // Rigidbody2D というコンポーネントを見るために必要、と覚えておこう
    // (GameObject の Component は、そのゲームオブジェクトが持つ実体のみならず、
    // 追加で持つ「特性」や「属性」なんかも、 "Component" とみなしている、と考えておく)
    private Rigidbody2D rb2d;

    // Vector2 に与えるスピード補正
    public float speed;

    // PickUp の回収数
    private int count;

    // テキスト表示への参照
    public Text countText;
    public Text winText;

    // PickUp 回収時の効果音への参照
    public AudioSource audioSource; // 自身にセットされた AudioSource への参照

    // なお、音を鳴らしたいのであれば、そのオブジェクトに AudioSource コンポーネントを追加し、
    // その中の AudioClip に .mp3 や .wav ファイルをくっつけよう

    void Start ()
    {
        rb2d = GetComponent<Rigidbody2D> ();
        rb2d.gravityScale = 0;
        count = 0;
        setCount();
        winText.text = "";

        audioSource = GetComponent<AudioSource>();
    }

	/// <summary>
    /// 今回は物理演算(2D Phisics)を使っているので、 Update ではなく FixedUpdate () で、
    /// 毎フレームのキー入力を受け付ける。
    /// Update () はそのフレームのレンダリングの直前に呼び出されるメソッドで、
    /// FixedUpdate () は、何らかの物理演算の発生する直前に呼び出される。
    /// </summary>
	void FixedUpdate ()
    {
        float moveHorizontal = Input.GetAxis ("Horizontal");
        float moveVertical = Input.GetAxis ("Vertical");

        Vector2 v2 = new Vector2 (moveHorizontal, moveVertical);

        rb2d.AddForce (v2 * speed);

        // なお、 2D 世界ではデフォルトだと二次元剛体には重力がかかり、
        // キー操作を受け入れる間もなく画面外に落下してしまう。
        // これを防ぐためには、Rigidbody2D.gravityScale プロパティを 0 にして、
        // 重力の影響をカットすること
    }

    /// <summary>
    /// トリガーコライダー(何らかのイベントを発火させる機能を持った衝突判定)が、
    /// 他のオブジェクトに接触した場合に発生するイベントを定義するメソッド
    /// </summary>
    /// <param name="other">衝突したと判定された、衝突先のオブジェクトの衝突判定</param>
    void OnTriggerEnter2D (Collider2D other)
    {
        if (!other.gameObject.CompareTag("PickUp"))
        {
            return; // PickUp タグを持たないオブジェクトなら強制リターン
        }

        other.gameObject.SetActive(false);
        // other.gameObject で、衝突先のオブジェクトへの参照を取得可能

        count++;

        setCount();

        // この時点ですでにセットされている Audio
        audioSource.Play();

        // もし 12 個ゲットできていればクリア
        if (count >= 12) {
            winText.text = "You Win!";
        }

    }

    private void setCount ()
    {
        countText.text = "Count: " + count.ToString ();
    }
}
