package com.cosmicbarrage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Enemy�N���X�̃C���X�^���X�𓝊��I�ɉ^�p���邽�߂̃N���X�B<br>
 * (�Ȃ�"Hive"=�u(�~�c�o�`��)���v�̈Ӗ��B<br>
 * ���̃N���X��Enemy�C���X�^���X�́u���v�Ƃ��Ďg���̂ŁA���̖������s����)<br><br>
 * ������Enemy�C���X�^���X�Q�́AArrayList�Ɋi�[���ꂻ�ꂼ�ꂪ�X���b�h�𔭍s���A<br>
 * ���Ԃ��ƂɎ���̉摜��ω������邱�ƂŃA�j���[�V������t���Ă����B<br>
 * ���������̎�@���̗p����ƁA���s����X���b�h����140�ɂ����B<br>
 * ���̑�ʂ̃X���b�h��JavaVM�ɑ���ȕ��ׂ�������悤�ŁAGameOver�������ɂ����āA<br>
 * �Ƃ�����ꕔ��Enemy�̏����Ɏ��s����s�s�����������B<br><br>
 * �����ŁA����EnemyHive�N���X���쐬���A���̃N���X��Enemy�N���X�����b�v����`�ŁA<br>
 * Enemy�N���X�̃C���X�^���X�̓����I�Ǘ����s���悤���t�@�N�^�����O�����s�B<br>
 * ���̃N���X��ArrayList�N���X���p�����A�X�ɕʃX���b�h���瓮���������N���X���������Ă���B<br>
 * ���̃N���X�ɒǉ����ꂽEnemy�́AEnemyHive�N���X�̔��s����ʃX���b�h��<br>
 * �A�j���[�V������������`�Ƃ��A140�������X���b�h�̏������A���̃N���X�̃X���b�h����1�X���b�h��<br>
 * ��ɏW�������邱�ƂŁAJavaVM�̕��׌y���Ɖǐ��E�ێ琫�̌������݂�B<br><br>
 * <b>�A�b�v�f�[�g(2012/12/18)</b><br>
 * ���\�b�h"existsAnyEnemies"��ǉ�
 * @author Yuichi Watanabe
 * @since 2012/12/17
 * @version 1.10 New Method Added on 2012/12/18
 * */
public class EnemyHive extends ArrayList<Enemy> implements DrawEventListener{

	/**
	 * �����o�ϐ�
	 * */
	//�e�N���X�ւ̎Q��
	private CosmicBarrage parentFrame;

	//���̏u�Ԃɂ����āA�G�O���t�B�b�N�̉����ڂ�\�����邩�������C���f�b�N�X�B
	//0~3�̂����ꂩ�̒l���Ƃ�
	int imageIndex = 0;

	//�ȑOEnemy�N���X�Ɏ�������Ă����X���b�h�B������O���N�����\�Ƃ��邽�߂ɁA
	//public�Ō^�錾
	public Thread imageIndexChanger;

	//���̃N���X���@�\���ێ����Ă��邩�ǂ�����\���t���O�B
	//���̃N���X�̃X���b�h�̃��[�v����ȂǂɎg��
	private boolean isActive = true;

	/**
	 * �V���A��ID
	 */
	private static final long serialVersionUID = 1L;

	/**�R���X�g���N�^�B�܂��X�[�p�[�N���X�̃R���X�g���N�^�����s���āA
	 * ���̑��������̏������s��*/
	EnemyHive(CosmicBarrage parentFrameInput){
		super();

		//�e�N���X�ւ̎Q�Ƃ��i�[
		parentFrame = parentFrameInput;

		//���̎Q�Ƃ����ƂɁADrawEventManager�Ɏ��g��o�^
		parentFrame.drawEventManager.addDrawEvent(this);

		//Enemy�C���X�^���X�ɃA�j���[�V�����������邽�߂ɁA
		//imageIndexChanger�ɑ΂��A���g�̓����N���X���C���X�^���X���B
		//�Ȃ��A�����N���X�̃R�[�h�̉ǐ��Ƃ����ϓ_����A
		//�����č���͓����N���X�Ɏ��g�̎Q�Ƃ�n��
		imageIndexChanger = new ImageIndexChangerBody(this);

	}

	/*
	 * �ȉ��A���\�b�h����ѓ����N���X��񋓂���B
	 * �񋓂��鏇�Ԃ�
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * 2.�ʏ�̃��\�b�h
	 * 3.�����N���X
	 * 4.�f�X�g���N�^���\�b�h
	 * �̏��Ƃ���B
	 * 1,2,3�̃O���[�v���ł́A���ꂼ��X��ABC���Ń����o������ł���B
	 * �I�[�o�[���[�h���ꂽ���\�b�h�́A�����Ȃ������瑽�����ɕ��ׂ�
	 * */

	/* ##############################
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * ##############################*/

	/**
	 * ����`��C�x���g���AEnemyHive�Ɏ����������\�b�h�B<br>
	 * ���̃��\�b�h���ł́A���ݎ��g�ɓo�^����Ă���Enemy�̃O���t�B�b�N���A<br>
	 * Enemy�̃����o�ϐ��ɉ�����for�߂̑�������ōs���B<br>
	 * Enemy��isAlive��true�ł���Ȃ�A�ʏ�̃O���t�B�b�N��`�悵�A<br>
	 * isAlive��false�ł���ꍇ�Ȃ�AEnemy��blastImage��`�悷��B<br>
	 * blastImage��`�悷��ꍇ�A�t���[�����Ƃ�blastImage�̕`��́A<br>
	 * EnemyHive�N���X�ł͂Ȃ��eEnemy�N���X���S������Ƃ��낪�~�\�ƌ�����
	 * */
	public void onDrawEvent(Image targetImage) {
		// TODO Auto-generated method stub

		//�n���ꂽ�C���[�W��Graphics���擾
		Graphics g = targetImage.getGraphics();

		//����g�����ƂɁA���ݎ��g�Ɋi�[����Ă���Enemy�N���X�̃C���X�^���X�ɑ�������B
		//Enemy��isAlive�t���O�𒲂ׂAfor�߂��g�p
		for (int i = 0; i < this.size(); i++){

			//���݃X�L��������Enemy�̎Q�Ƃ��i�[
			Enemy targetEnemy = this.get(i);

			//���݃`�F�b�N����Enemy�������Ă���ꍇ
			if (targetEnemy.isAlive == true ) {
				//���C���[�W�̒��ŁA�ǂ̗̈��\�����邩���������W�́A
				//�R�[�h�̉ǐ��̊ϓ_���炢�����񉼕ϐ��Ɋi�[
				int originalImageX
				= targetEnemy.displayingRegion.x;
				int originalImageY
				= targetEnemy.displayingRegion.y;
				int originalImageXEnd
				= targetEnemy.displayingRegion.x + targetEnemy.displayingRegion.width;
				int originalImageYEnd
				= targetEnemy.displayingRegion.y + targetEnemy.displayingRegion.height;

				//��L�̉��ϐ������ƂɁA�w�肳�ꂽ�̈��`��
				g.drawImage(targetEnemy.enemyImage,
							targetEnemy.x,
							targetEnemy.y,
							targetEnemy.x + targetEnemy.width,
							targetEnemy.y + targetEnemy.height,
							originalImageX,
							originalImageY,
							originalImageXEnd,
							originalImageYEnd,
							parentFrame);
			}
			//���݃`�F�b�N����Enemy������ł���ꍇ
			else if ( (targetEnemy.isAlive == false)
					&& (targetEnemy.blastImage != null) ) {
				g.drawImage(
						targetEnemy.blastImage,
						(targetEnemy.x - 8), (targetEnemy.y - 8),
						(targetEnemy.x + 40), (targetEnemy.y + 40),
						0, 0,
						48, 48,
						parentFrame);
			}//if�ߏI��

		}//Enemy���������for�ߏI��

		//g��j��
		g.dispose();

	}//onDrawEvent���\�b�h�I��

	/* ############
	 * 2.�ʏ�̃��\�b�h
	 * ############*/

	/**
	 * �w�肳�ꂽ�̈���ɁA�G�����݂��邩�ǂ������m���߂郁�\�b�h�B<br>
	 * �w�肳�ꂽ�̈���ɓG�������ꍇtrue���Ԃ�A���Ȃ��ꍇfalse���Ԃ�B<br>
	 * �Q�[����ʒ��ɐV�����I�u�W�F�N�g��ǉ�����ہA<br>
	 * ���̃I�u�W�F�N�g���G�ƂԂ���Ȃ����ǂ����`�F�b�N����Ƃ��ȂǂɎg���B<br>
	 * Ball�N���X��isHitWithThisRegion���\�b�h�̃A���S���Y���𗬗p�B
	 * */
	public boolean existsAnyEnemies(Rectangle searchingRegion) {
		boolean result = false;//�t���O��false����X�^�[�g

		//for�߂��g���A���ݑ��݂��Ă���S�Ă̓G�ɂ��āA���݂��`�F�b�N
		for (int i = 0; i < this.size(); i++) {

			//���݃X�L�������Ă���G�̍��W�f�[�^�����ϐ��Ɋi�[
			int enemyX = this.get(i).x;
			int enemyY = this.get(i).y;
			int enemyWidth = this.get(i).width;
			int enemyHeight = this.get(i).height;

			//���̎��_�ŁA�ʃt���O��p�ӁB���݂̓G�̈�ƃq�b�g���Ă��邩�ǂ���
			boolean isHit = true;

			//���̓G���Ώۗ̈�̍����ɔ�яo���Ă���ꍇ
			if (enemyX + enemyWidth < searchingRegion.x) {
				isHit = false;
			}
			//���̓G���Ώۗ̈�̏㑤�ɔ�яo���Ă���ꍇ
			else if (enemyY + enemyHeight < searchingRegion.y) {
				isHit = false;
			}
			//���̓G���Ώۗ̈�̉����ɔ�яo���Ă���ꍇ
			else if (searchingRegion.y + searchingRegion.height < enemyY) {
				isHit = false;
			}
			//���̓G���Ώۗ̈�̉E���ɔ�яo���Ă���ꍇ
			else if(searchingRegion.x + searchingRegion.width < enemyX) {
				isHit = false;
			}//������`�F�b�N��if�ߏI��

			//���̎��_��isHit��true�̂܂܂Ȃ�Aresult��true�ɃZ�b�g����break
			if (isHit == true) {
				result = true;
				break;
			}

		}//for�ߏI��

		//�ȏ�̌��ʂ�ԋp
		return result;

	}

	/* ##########
	 * 3.�����N���X
	 * ##########*/

	/**
	 * Enemy�N���X����ڐA�����AImageIndex�����Ԃ��Ƃɏ���������X���b�h�𔭍s����N���X�B
	 * �Ȃ��A����̓X���b�h�{�̓��ŁAEnemyHive��size���Q�Ƃ��Ȃ���΂����Ȃ��s����A
	 * ���̂܂܂���for�̏����ߓ���"i < size()"�ȂǁA���ǐ��̒Ⴂ�R�[�h������������Ă��܂��B
	 * ������������ړI�ŁA�����Ă��̃N���X�͐e�ł���EnemyHive�̎Q�Ƃ��󂯎��A
	 * ����������o��parent�Ɋi�[���邱�ƂŁA��L��for�߂̒��g��"i < parent.size()"�Ȃǂ�
	 * ������悤�ɃR�[�f�B���O���Ă���B
	 * */
	private class ImageIndexChangerBody extends Thread{

		/*�e�N���X�̎Q��*/
		private EnemyHive parent;

		/**
		 * �R���X�g���N�^�𖾎��I�ɍ쐬���A�����Őe�N���X�̎Q�Ƃ������o�ϐ��Ɋi�[
		 * */
		ImageIndexChangerBody(EnemyHive parentInput){
			super();
			parent = parentInput;
		}

		@Override
		public void run(){

			//imageIndex�𖾎��I�ɏ���
			imageIndex = 0;

			//���̌�A���̃N���X���A�N�e�B�u�ł�����薳�����[�v
			while (isActive == true){

				//imageIndex�𑀍�
				imageIndex = imageIndex + 1;

				if (imageIndex >= 4){
					imageIndex = 0;
				}

				//��L�̑���Ŋm�肵���l���A
				//���݂��̃N���X�Ɋi�[����Ă���Enemy�ɂ��ẮA
				//���̏u�Ԃ�displayingRegion�����肳���B
				//�Ȃ��A�SEnemy�ւ̃A�N�Z�X�ɂ�for���g��
				for (int i = 0; i < parent.size(); i++) {

					if (imageIndex == 0) {
						parent.get(i).displayingRegion.setLocation(0, 0);
					}else if (imageIndex == 1) {
						parent.get(i).displayingRegion.setLocation(32, 0);
					}else if (imageIndex == 2) {
						parent.get(i).displayingRegion.setLocation(0, 32);
					}else {
						parent.get(i).displayingRegion.setLocation(32, 32);
					}

				}//�SEnemy�ɃA�N�Z�X����for�ߏI��

				//�X���[�v
				try {
					Thread.sleep(200);//�����ꂪ����Ă���悤�Ɍ�����œK����(?)
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//try�ߏI��
			}//�������[�v�I��

		}//run���\�b�h�I��
	}//ImageIndexChangerBody�N���X�I��

	/* ###############
	 * 4.�f�X�g���N�^���\�b�h
	 * ###############*/


}//EnemyHive�N���X�I��

/*
 * �Ȃ��A�ŏ���Runnable�N���X���������āA���̃N���X���̂ɃX���b�h���s�@�\��t���悤���Ƃ�
 * �l��������ǂ��A���̃v���W�F�N�g���ł́A�X���b�h�̎�����Thread���p�����������N���X��
 * �s���Ă���̂ŁA����͂���ɓ���
 * */
