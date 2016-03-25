package com.cosmicbarrage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * �Q�[����ʏ�ɕ\������鐔���̃O���t�B�b�N����������N���X�B
 * ��ʂɕ\�����������l���͂���ƁA���̐����ɑΉ������摜���N���X���ō������A
 * ���̌��ʂ̉摜��ԋp����@�\������
 * @author Yuichi Watanabe
 * @since 2012/12/11
 * @version 1.00
 * */
public class DigitManager implements ImageObserver {

	//�C���[�W�摜�\�[�X�B�R���X�g���N�^�N�����A�����̒��ɐ����摜�̃��\�[�X���Z�b�g
	private BufferedImage softDigitSource;
	private BufferedImage hardDigitSource;


	/**
	 * �R���X�g���N�^
	 * */
	public DigitManager(){
		softDigitSource = null;
		hardDigitSource = null;

		try {
			softDigitSource = ImageIO.read(getClass().getResource("Digits(Soft).bmp"));
			hardDigitSource = ImageIO.read(getClass().getResource("Digits(Hard).bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**��͂��ꂽ���������Ƃɉ摜���������A�������ꂽ���l�̉摜��ԋp���郁�\�b�h�B<br>
	 * ���\�b�h���I�[�o�[���[�h����Ă��邽�߁A�I�v�V�����w�肪�\�B<br>
	 * �Ȃ��A����̃R�[�h�����x�������͔̂ς킵���̂ŁA�����{�̂��̂��̂́A<br>
	 * private���\�b�h��getDigitGraphicBody�ɂ�点�A���ʂ����̂܂ܕԋp������*/
	public BufferedImage getDigitGraphic(Integer input){

		DigitFontOption option1 = DigitFontOption.HARD;//�f�t�H���g�̓n�[�h
		DigitFormatOption option2 = DigitFormatOption.NORMAL;//�f�t�H���g�̓m�[�}��

		return getDigitGraphicBody(input, option1, option2);

	}
	/**��͂��ꂽ���������Ƃɉ摜���������A�������ꂽ���l�̉摜��ԋp���郁�\�b�h�B<br>
	 * ���\�b�h���I�[�o�[���[�h����Ă��邽�߁A���܂��܂ȃI�v�V�����w�肪�\�B<br>
	 * �Ȃ��A����̃R�[�h�����x�������͔̂ς킵���̂ŁA�����{�̂��̂��̂́A<br>
	 * private���\�b�h��getDigitGraphicBody�ɂ�点�A���ʂ����̂܂ܕԋp������*/
	public BufferedImage getDigitGraphic(Integer input, DigitFontOption digitFontOption){

		DigitFontOption option1 = digitFontOption;
		DigitFormatOption option2 = DigitFormatOption.NORMAL;//�f�t�H���g�̓m�[�}��

		return getDigitGraphicBody(input, option1, option2);

	}
	/**��͂��ꂽ���������Ƃɉ摜���������A�������ꂽ���l�̉摜��ԋp���郁�\�b�h�B<br>
	 * ���\�b�h���I�[�o�[���[�h����Ă��邽�߁A���܂��܂ȃI�v�V�����w�肪�\�B<br>
	 * �Ȃ��A����̃R�[�h�����x�������͔̂ς킵���̂ŁA�����{�̂��̂��̂́A<br>
	 * private���\�b�h��getDigitGraphicBody�ɂ�点�A���ʂ����̂܂ܕԋp������*/
	public BufferedImage getDigitGraphic(	Integer input,
											DigitFontOption digitFontOption,
											DigitFormatOption digitFormatOption){

		DigitFontOption option1 = digitFontOption;
		DigitFormatOption option2 = digitFormatOption;

		return getDigitGraphicBody(input, option1, option2);

	}


	/**
	 * getDigitGraphic�̖{�������e�B
	 * getDigitGraphic���\�b�h�̈��n���A�����̈�����Ƃɐ��l�̉摜����������
	 * */
	private BufferedImage getDigitGraphicBody(	Integer input,
												DigitFontOption digitFontOption,
												DigitFormatOption digitFormatOption){
		//��͒l�𕶎��񉻂����l
		String inputAsString = input.toString();

		//�ԋp���錴�C���[�W���쐬
		BufferedImage outputImage = null;

		//input�̎蒼���B����digitFormatOption��NORMAL�ȊO�Ȃ�A
		//���̃N���X�̃��\�b�h���g���āA�R���}��ǉ�������'��"��U���Ď��Ԃɕϊ�
		if (digitFormatOption == DigitFormatOption.WITH_COMMA) {
			inputAsString = this.insertCommasIntoDigits(input);
		}else if ( digitFormatOption == DigitFormatOption.TIME ) {
			inputAsString = this.convertCStoMMSSss(input);
		}

		//�܂��Ainput�̌���=�������𒲂ׁAoutputImage�̑傫�����m��B
		//1���ɂ��Awidth��5px�AHeight��10px�ŌŒ�
		int degree = inputAsString.length();
		outputImage = new BufferedImage(degree * 5, 10, BufferedImage.TYPE_INT_ARGB);

		//���ŁA�C���[�W����̏I��������̒i�K�ŁAGraphics���擾
		Graphics g = outputImage.createGraphics();

		//���ɁA�w�肳�ꂽ�I�v�V��������ASoft��Hard�ǂ���̃t�H���g���g��������
		BufferedImage usingFont;
		if (digitFontOption == DigitFontOption.SOFT) {
			usingFont = softDigitSource;
		}
		else {
			usingFont = hardDigitSource;
		}

		//�ȏ�̃f�[�^���Afor�߂��g����͂��ꂽ�������ꕶ���ꕶ���X�L�����B
		//�Ή����鐔�����AoutPutImage�ɏ�������ł���
		for (int i = 0; i < inputAsString.length(); i++) {
			int xBegin = 0;

			//String�̓I�u�W�F�N�g�Ȃ̂ŁA�ʓ|�������炸equals���\�b�h���g����

			//"1"��\������ꍇ
			if (inputAsString.substring(i, i + 1).equals("1")) {
				xBegin = 0;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"2"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("2")) {
				xBegin = 5;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"3"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("3")) {
				xBegin = 10;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"4"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("4")) {
				xBegin = 15;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"5"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("5")) {
				xBegin = 20;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"6"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("6")) {
				xBegin = 25;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"7"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("7")) {
				xBegin = 30;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"8"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("8")) {
				xBegin = 35;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"9"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("9")) {
				xBegin = 40;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"0"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("0")) {
				xBegin = 45;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//","��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals(",")) {
				xBegin = 50;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"\'"��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("\'")) {
				xBegin = 55;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}
			//"\""��\������ꍇ
			else if (inputAsString.substring(i, i + 1).equals("\"")) {
				xBegin = 60;
				g.drawImage(usingFont,
							i * 5, 0, (i + 1) * 5, 10,
							xBegin, 0, (xBegin + 5), 10,
							this);
			}//if�ߏI��

		}//for�ߏI��


		return outputImage;

	}//getDigitGraphicBody���\�b�h�I��

	/**
	 * ��͂��ꂽ�b��(�P�ʂ�cs=�Z���`�b=0.01�b)�𕶎���ɏ��������郁�\�b�h�B<br>
	 * ����������̕�����́u��'�b"�Z���`�b�v�Ƃ����`���̕�����ł���
	 * */
	private String convertCStoMMSSss(Integer centiSeconds){
		//�܂��͓�͂��ꂽ�b����6000�Ŋ���A��͒l���u���v���Z�ŉ��������v�Z
		Integer minutes = centiSeconds / 6000;

		//����minutes * 6000�̒l��centiSeconds�����A
		//�u�b�v�Ɓu�Z���`�b�v�̕����݂̂��c��
		centiSeconds = centiSeconds - minutes * 6000;

		//���x�́A�c�����l��100�Ŋ���A�u�b�v�̕��������������v�Z����
		Integer seconds = centiSeconds /100;

		//��͂肱���seconds * 100������āA
		//�����́u�Z���`�b�v�̕��������������v�Z����
		centiSeconds = centiSeconds - seconds * 100;

		//�ȏ�A���܂����u���v�u�b�v�u�Z���`�b�v�̒l�𕶎���ɕϊ����A
		//���̊Ԃ�'��"������Ō������ꂽ���ʂ�ԋp

		String resultString
		= new String(minutes.toString() + "\'"
					+ seconds.toString() + "\""
					+ centiSeconds.toString());

		return resultString;
	}

	/**
	 * ��͂��ꂽ�����ɃR���}��}��������ԋp���郁�\�b�h�B<br>
	 * �R���}�͐��l��4���ȏ�ł���ꍇ�A3�������ɒǉ������
	 * */
	private String insertCommasIntoDigits(Integer Digits){
		//StringBuffer���쐬���A�����̒����I�Ȓǉ��ɔ�����
		StringBuffer createdBuffer = new StringBuffer(Digits.toString());

		/*���̃o�b�t�@�ɑ΂���R���}�̒ǉ��A���S���Y���́A�ȉ��̒ʂ�Ƃ���B
		�܂��o�b�t�@�̕�����擪���珇�Ɉꕶ�����X�L�������āA
		�ŏ��ɃR���}���������ʒu���L�^����B
		(�����Ō�܂ŃX�L�������ăR���}��������Ȃ�������A�o�b�t�@�̍Ō�����L�^����)
		�����āA���̈ʒu����3�����Ԃ��ނ����ӏ��ɃR���}��ǉ��B
		���ꂪ�I�������A�ēx�o�b�t�@�̐擪���當�����X�L�����B������J��Ԃ��B
		����3������ނ������ʁA�C���f�b�N�X��0�����ɂ܂ł͂ݏo���ꍇ�A
		���̎��_�ŃR���}�̑}��͏I��������̂Ƃ݂Ȃ��āA�������I��������*/

		//scanIndex�̏���l�́AcreatedBuffer�̒���-1�Ƃ���
		int scanIndex = createdBuffer.length();

		//scanIndex - 3��0���傫���ԁA�������J��Ԃ�
		while ( (scanIndex - 3) > 0 ) {

			//�o�b�t�@�̃X�L����
			for (int i = 0; i < createdBuffer.length(); i++) {

				//i�����ڂ��J���}�Ȃ�A���̈ʒu��scanIndex�Ɋi�[
				if (createdBuffer.substring(i, i + 1).equals(",")) {
					scanIndex = i;
					break;
				}

			}

			//scanIndex-3�̈ʒu�ɃR���}��ǉ�
			if ( (scanIndex - 3) > 0 ) {
				createdBuffer.insert((scanIndex - 3), ",");
			}

		}

		System.out.println(createdBuffer.toString());

		//�ȏ�̑���łł����o�b�t�@��string�ɕϊ����ĕԋp
		return createdBuffer.toString();
	}

	/* ######
	 * �񋓌^
	 * ######*/

	/**
	 * DigitManager���ԋp���鐔�l�̉摜�ɂ��āA<br>
	 * �t�H���g��Soft��Hard�A�����ꂩ���w�肷��I�v�V����
	 * */
	enum DigitFontOption{
		/**Soft�t�H���g*/
		SOFT,
		/**Hard�t�H���g*/
		HARD
	}

	/**
	 * DigitManager���ԋp���鐔�l�̉摜�ɂ��āA<br>
	 * ���l�̕\����@�̃t�H�[�}�b�g���w�肷��I�v�V����
	 * */
	enum DigitFormatOption{
		/**�m�[�}��*/
		NORMAL,
		/**�R���}��؂�B4���𒴂��鐔���́A����3���Ƃ�","��}����*/
		WITH_COMMA,
		/**���ԕ\�L�B�P�ʂ�cs(�Z���`�b=0.01�b=10�~���b)�B<br>
		 * ���ƕb�̒P�ʂ̊ԂɁA���ꂼ��'��"���}����*/
		TIME
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
	/*�]�k�����A���̃N���X��������BufferedImage�̕`����������������Ȃ�A
	 * drawImage�̈�imageObserver�ɁA�摜���������N���X���w�肷��̂ł͂Ȃ��A
	 * ���̃N���X��imageObserver�C���^�[�t�F�C�X���������Ă��܂��Ƃ����������B
	 * ����͂�������s����*/

}
