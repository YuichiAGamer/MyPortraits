package com.cosmicbarrage;

/**
 * 2D�̃O���t�B�b�N�X��ňړ����镨�̂̃x�N�g�����Ǘ�����N���X�B
 * ���̂̃x�N�g����x������y�����A�����Đ錾��������΃X�J���[�ʂ�錾�ł���B
 * ����������ƁA�x�N�g���ŕ��̂̐i�s�����A�X�J���[�ʂŕ��̂̎���Α��x���w��ł���A
 * �Ƃ������ƁB
 * �X�J���[�ʂ𓯎��ɐ錾���Ȃ��ꍇ�A�X�J���[��x�����̓��+y�����̓��̕�������
 * �����v�Z����邪�A�X�J���[�ʂ𓯎��ɐ錾�����ꍇ�Ax��y�̔䗦�����ɕۂ����܂܁A
 * �X�J���[�ʂ��w�肳�ꂽ�l�ɂȂ�悤�ɁAx��y�͎����I�ɒ��������B
 * �Ȃ�������"Vector"�����A�R���N�V������Vector�Ɠ����ŕ���킵���̂ŁA
 * "EntityVector(���̃x�N�g��)"�Ɖ���
 * */
class EntityVector{
	
	/**
	 * �t�B�[���h(�����o�ϐ�)
	 * */
	private double x;
	private double y;
	private double scholar;
	//����͂����܂Ō����Ȑ��l���͗v������Ȃ����A
	//Math.sqrt���\�b�h���g���֌W�ŁA�t�B�[���h�̌^�͑S��float�ł͂Ȃ�double�Ő錾
	
	/**
	 * �R���X�g���N�^
	 * */
	//�X�J���[��錾���Ȃ��ꍇ
	EntityVector(int x, int y){
		this.x = x;
		this.y = y;
		this.scholar = Math.sqrt(x * x + y * y );
	}
	EntityVector(int x, int y, int scholar){
		this.x = x;
		this.y = y;
		this.scholar = scholar;//�b����Ƃ��đ��
		//�X�J���[��Ɨ��ϐ��Ax,y���]���ϐ��Ƃ��āAx,y���Čv�Z����A���S���Y����
		//�T�C�Y���傫���̂ŁA���N���X�̕ʃ��\�b�h�Ōv�Z
		double coefficient = getScholarModCoefficient();
		
		this.x = x * coefficient;
		this.y = y * coefficient;
		
	}
	
	
	
	private double getScholarModCoefficient() {
		double coefficient = 1;//���̃��[�v���ł́A�␳�O�̌W���B�����l��1
		double newCoefficient = 1;//���̃��[�v���ł́A�␳��̌W���B�����l��1
		double stride = 0.1;//�W�����������������邩�����߂�A���ݕ��̌W��
		int signum = 1;//�W���𐳂̕����ɑ��₷�����̕����Ɍ��炷�������߂�l�B+1��-1���w��
		double myX = this.x;//�����o�ϐ���x���瓾���ʑ�
		double myY = this.y;//�����o�ϐ���x���瓾���ʑ�
		double myScholar
			= Math.sqrt(myX * myX + myY * myY);//���͂��ꂽx�Ay����Z�o�����{���̃X�J���[��
		
		while(Math.abs(myScholar * coefficient - this.scholar) > 0.01){
			//myScholar�~�␳�l�ƁA���[�U�[�̎w�肵��scholar�̒l���r���A
			//�����ǂ���̕����ɐi�ނ�����
			if (myScholar * coefficient >= this.scholar){
				signum = -1;
			}
			else if (myScholar * coefficient < this.scholar){
				signum = 1;
			}
			
			//��L��if�߂ŋ��܂���signum�̕����ɍ��킹�āA�W����␳
			newCoefficient = coefficient + stride * signum;
			
			/*����coefficient��newCoefficient�ł��ꂼ��|���Z����myScholar�̒l���A
			this.scholar�̒l���܂����ŕʂ̏ꏊ�ɂ���΁A�␳���newEfficient��
			�^�l���щz�������ƂɂȂ�B���ꂪ�N�����ꍇ�A�X�ɉ��̐��x���グ�邽�߂ɁA
			stride��1/10�ɂ���*/
			
			//Scholar�����̕����Ɂu��щz���v���ꍇ
			if((myScholar * coefficient < this.scholar) &&
					(myScholar * newCoefficient > this.scholar)){
				stride = stride / 10;
			}
			//Scholar�����̕����Ɂu��щz���v���ꍇ
			if((myScholar * coefficient > this.scholar) &&
					(myScholar * newCoefficient < this.scholar)){
				stride = stride / 10;
			}
			//��Lif�߂͂܂Ƃ߂悤�Ǝv���Έ�ɂ��ł��邪�A�R�[�h�̉ǐ����l��
			//�񕪊����ĕ\�L
			
			//���[�v�̍Ō�ɁA����newCoefficient�ɑ�������l��coefficient��
			//�ڂ��ς�
			coefficient = newCoefficient;
		}//���[�v�I��				
		
		//�ȏ�̌v�Z���ʂ�Ԃ�
		// TODO Auto-generated method stub
		return coefficient;
	}
	
	/*�ȉ��Ax,y,scholar�̃Q�b�^�[&�Z�b�^�[*/
	public int getX(){
		return (int)this.x;
	}
	
	public void setX(int x){
		this.x = x;
		//x��������������X�J���[�ʂ��Čv�Z
		this.scholar = Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public int getY(){
		return (int)this.y;
	}
	
	public void setY(int y){
		this.y = y;
		//y��������������X�J���[�ʂ��Čv�Z
		this.scholar = Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public int getScholar(){
		return (int)this.scholar;
	}
	
	public void setScholar(int newScholar){
		//�X�J���[�͕�������肦�Ȃ��̂ŁA�������w�肳�ꂽ���O�𓊂���
		if (newScholar < 0){
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//��O���������Ȃ���Ώ����𑱍s		
		
		this.scholar = newScholar;
		//�X�J���[��Ɨ��ϐ��Ax,y���]���ϐ��Ƃ��āAx,y���Čv�Z����A���S���Y����
		//�T�C�Y���傫���̂ŁA���N���X�̕ʃ��\�b�h�Ōv�Z
		double coefficient = getScholarModCoefficient();
		
		this.x = x * coefficient;
		this.y = y * coefficient;		
	}
	
}//Vector�N���X�I��
