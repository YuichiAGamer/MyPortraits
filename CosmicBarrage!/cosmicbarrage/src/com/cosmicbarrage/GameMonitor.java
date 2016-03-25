package com.cosmicbarrage;

import java.awt.Rectangle;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ���ݐi�s���̃Q�[���ɂ����链�_��o�ߎ��ԁA����I�ɔ�������C�x���g�Ȃǂ��Ǘ�����N���X�B<br>
 * �Q�[�����n�܂��Ă���̌o�ߎ��Ԃ����̃N���X�ŋL�^����B<br>
 * 2012/12/18���݂͖����������A��ʏ�ɓ��_��\�����Ă����R���\�[���@�\�Ȃǂ�<br>
 * �ǂ��ǂ��������čs���\��B
 * @author Yuichi Watanabe
 * @since 2012/12/18
 * @version 1.00
 * */
public class GameMonitor {

	/**
	 * �����o�ϐ�
	 * */
	public long ellapsedTime;//�o�ߎ���(�~���b�P�ʁB�����Ԃ̌v����z�肵long���g�p)
	public Timer timer;//�O������^�C�}�[���N�����邽�߂̌^�錾
	private CosmicBarrage parentFrame;//�e�N���X�ւ̎Q��
	
	/**
	 * �R���X�g���N�^
	 * */
	GameMonitor(CosmicBarrage parentFrameInput){
		//�o�ߎ��Ԃ�0�Ƀ��Z�b�g
		ellapsedTime = 0;
		//timer�^�Ƀ^�X�N���Z�b�g
		timer = new Timer();
		//�e�N���X�̎Q�Ƃ��Z�b�g
		parentFrame = parentFrameInput;
	}
	
	/**�^�C���J�E���g�J�n�p�̃��\�b�h*/
	public void startTimer(){
		timer.schedule(new gameTimerTask(), 0, 10);
	}
	
	/**�����N���X*/
	class gameTimerTask extends TimerTask{

		
		//10�~���b�o�߂��邲�ƂɁAellapsedTime��10�ǉ�����
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ellapsedTime = ellapsedTime + 10;
			
			//�o�ߎ���10�b���ƂɁA�V���ȃ{�[���𐶐�����C�x���g���N����
			if ( (ellapsedTime % 10000 == 0) ) {
				genarateNewBall();
			}
			
		}//run���\�b�h�I��		
		
	}

	/**
	 * �V���ȃ{�[�����{�[�����X�g�ɒǉ����A���������\�b�h�B<br>
	 * �܂��G�̑��݂��Ȃ�16*16�̃X�y�[�X�������_���ɒT���o���A���̍��W��<br>
	 * EffectDrawer�N���X��"ballGenerationEffect"���N�����ăA�j���[�V�����B<br>
	 * �Ō�ɁA�{�[���𓮂������߂̃X���b�h���N�����ďI���A�Ƃ�������ɂȂ�
	 * */
	public void genarateNewBall() {
		// TODO Auto-generated method stub
		
		//�܂��A���W�������_���ɑI��
		Random random = new Random();
		Rectangle myRegion = null;
		boolean isfittingRegion = false;
		
		//�G�̂��Ȃ��̈���������Ă�܂ŁA���[�v���J��Ԃ�
		while(isfittingRegion == false) {
			//�����_���̈�͂���B
			//�{�[���͐������A�K��������̃x�N�g�������悤�ȃ����_�}�C�Y��
			//�󂯂Ă���̂ŁA��ʉ��M���M���Ő�������Ă����v
			int randomX = random.nextInt(640 - 16);
			int randomY = random.nextInt(480 - 16);
			
			//��`�̈���m��
			myRegion 
			= new Rectangle(randomX, randomY, randomX + 16, randomY + 16);
			
			//���̗̈�ɂ��āA�G�̑���/�񑶍݂̔���
			isfittingRegion = ! parentFrame.enemyHive.existsAnyEnemies(myRegion);
			
		}
		
		//���̗̈�ŁA�{�[���𐶐��B���W�͏�L�̗̈���蓮�w��
		Ball newBall = new Ball(parentFrame,
								parentFrame.serialID,
								myRegion.x,
								myRegion.y);
		
		parentFrame.ballList.add(newBall);
		
		parentFrame.serialID = parentFrame.serialID + 1;
		
		//������ballGenerationEffect���N���B
		//�Ȃ��A������̃��\�b�h����́AeffectDrawer�̃��\�b�h�ɑ΂���join���\�b�h��
		//�g���Ȃ��̂ŁA����ball�̎Q�Ƃ��������̃��\�b�h�ɓn���A
		//������ŋN�����Ă��炤
		parentFrame.effectDrawer.ballGenerationEffect(newBall);		
		
	}
	
}
