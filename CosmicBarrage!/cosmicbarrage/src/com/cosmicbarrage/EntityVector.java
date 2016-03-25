package com.cosmicbarrage;

/**
 * 2Dのグラフィックス上で移動する物体のベクトルを管理するクラス。
 * 物体のベクトルのx成分とy成分、そして宣言したければスカラー量を宣言できる。
 * 言い換えると、ベクトルで物体の進行方向、スカラー量で物体の持つ絶対速度を指定できる、
 * ということ。
 * スカラー量を同時に宣言しない場合、スカラーはx成分の二乗+y成分の二乗の平方根で
 * 自動計算されるが、スカラー量を同時に宣言した場合、xとyの比率を一定に保ったまま、
 * スカラー量が指定された値になるように、xとyは自動的に調整される。
 * なお旧名は"Vector"だが、コレクションのVectorと同じで紛らわしいので、
 * "EntityVector(実体ベクトル)"と改名
 * */
class EntityVector{
	
	/**
	 * フィールド(メンバ変数)
	 * */
	private double x;
	private double y;
	private double scholar;
	//今回はそこまで厳密な数値解は要求されないが、
	//Math.sqrtメソッドを使う関係で、フィールドの型は全てfloatではなくdoubleで宣言
	
	/**
	 * コンストラクタ
	 * */
	//スカラーを宣言しない場合
	EntityVector(int x, int y){
		this.x = x;
		this.y = y;
		this.scholar = Math.sqrt(x * x + y * y );
	}
	EntityVector(int x, int y, int scholar){
		this.x = x;
		this.y = y;
		this.scholar = scholar;//暫定解として代入
		//スカラーを独立変数、x,yを従属変数として、x,yを再計算するアルゴリズムは
		//サイズが大きいので、自クラスの別メソッドで計算
		double coefficient = getScholarModCoefficient();
		
		this.x = x * coefficient;
		this.y = y * coefficient;
		
	}
	
	
	
	private double getScholarModCoefficient() {
		double coefficient = 1;//そのループ内での、補正前の係数。初期値は1
		double newCoefficient = 1;//そのループ内での、補正後の係数。初期値は1
		double stride = 0.1;//係数をいくつ増減させるかを決める、刻み幅の係数
		int signum = 1;//係数を正の方向に増やすか負の方向に減らすかを決める値。+1か-1を指定
		double myX = this.x;//メンバ変数のxから得た写像
		double myY = this.y;//メンバ変数のxから得た写像
		double myScholar
			= Math.sqrt(myX * myX + myY * myY);//入力されたx、yから算出した本来のスカラー量
		
		while(Math.abs(myScholar * coefficient - this.scholar) > 0.01){
			//myScholar×補正値と、ユーザーの指定したscholarの値を比較し、
			//正負どちらの方向に進むか決定
			if (myScholar * coefficient >= this.scholar){
				signum = -1;
			}
			else if (myScholar * coefficient < this.scholar){
				signum = 1;
			}
			
			//上記のif節で求まったsignumの方向に合わせて、係数を補正
			newCoefficient = coefficient + stride * signum;
			
			/*もしcoefficientとnewCoefficientでそれぞれ掛け算したmyScholarの値が、
			this.scholarの値をまたいで別の場所にあれば、補正後のnewEfficientは
			真値を飛び越えたことになる。これが起きた場合、更に解の精度を上げるために、
			strideを1/10にする*/
			
			//Scholarが正の方向に「飛び越え」た場合
			if((myScholar * coefficient < this.scholar) &&
					(myScholar * newCoefficient > this.scholar)){
				stride = stride / 10;
			}
			//Scholarが負の方向に「飛び越え」た場合
			if((myScholar * coefficient > this.scholar) &&
					(myScholar * newCoefficient < this.scholar)){
				stride = stride / 10;
			}
			//上記if節はまとめようと思えば一つにもできるが、コードの可読性を考え
			//二分割して表記
			
			//ループの最後に、現在newCoefficientに代入した値をcoefficientに
			//移し変え
			coefficient = newCoefficient;
		}//ループ終了				
		
		//以上の計算結果を返す
		// TODO Auto-generated method stub
		return coefficient;
	}
	
	/*以下、x,y,scholarのゲッター&セッター*/
	public int getX(){
		return (int)this.x;
	}
	
	public void setX(int x){
		this.x = x;
		//xを書き換えたらスカラー量も再計算
		this.scholar = Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public int getY(){
		return (int)this.y;
	}
	
	public void setY(int y){
		this.y = y;
		//yを書き換えたらスカラー量も再計算
		this.scholar = Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public int getScholar(){
		return (int)this.scholar;
	}
	
	public void setScholar(int newScholar){
		//スカラーは負数を取りえないので、負数を指定されたら例外を投げる
		if (newScholar < 0){
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//例外が投げられなければ処理を続行		
		
		this.scholar = newScholar;
		//スカラーを独立変数、x,yを従属変数として、x,yを再計算するアルゴリズムは
		//サイズが大きいので、自クラスの別メソッドで計算
		double coefficient = getScholarModCoefficient();
		
		this.x = x * coefficient;
		this.y = y * coefficient;		
	}
	
}//Vectorクラス終了
