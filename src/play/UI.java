package play;

import java.awt.Toolkit;
import controls.Button;
import field.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import shape.AbstractShape;
import shape.*;
import sound.SoundSystem;

public class UI extends Application {
	Label speed;
	double initx, inity;
	SoundSystem sound;
	long then = 0;
	int pieceNo = 0;
	String fifty = "";
	public int scores = 0;
	Color stock = Color.PINK;
	Color colors[] = new Color[5];
	double time = 0.5;
	int times = 0;
	Preview preview;
	Stage ps;
	//�ж��������޲���
	boolean soundWorks = false;
	static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
			SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
			SCENE_HEIGHT = 5 * SCREEN_HEIGHT / 6, SCENE_WIDTH = 4 * SCENE_HEIGHT / 3, FIELD_HEIGHT = SCENE_HEIGHT - 50,
			FIELD_WIDTH = FIELD_HEIGHT / 2;
	AbstractShape l;
	Position init = new Position(4, -4);
	AnimationTimer timer;
	//�ж��Ƿ�����ͣ��ť
	boolean paused = true;
	Field field;
	Label points;
	Button p;
	Button r;

	@Override
	public void start(Stage stage) {
		//��ʼ�����а�ť����ʽ��
		Button.init(SCREEN_HEIGHT);
		ps = stage;
		sound = new SoundSystem();
//		shuffle();
		initColors();
		//����ˮƽ���
		HBox root = new HBox(SCREEN_WIDTH * 0.03);
		//����
		root.setAlignment(Pos.CENTER);
		//�ŵ�����
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

		//��ʼ������ĳߴ磬�����10������ɣ���ÿ������ĳߴ�Ϊ���/10
		Block.init(FIELD_WIDTH / 10);
		//newһ��field�࣬������Ϸ���ڴ�С
		field = new Field(Block.SIZE*10, Block.SIZE*20, SCENE_HEIGHT);
		//���崹ֱ�����Ϊ�������
		VBox controls = new VBox(SCREEN_HEIGHT * 0.04);

		//����ˮƽ��壬������ʾ��һ������
		HBox cent = new HBox(20);
		cent.setAlignment(Pos.CENTER);
		preview = new Preview(Block.SIZE * 5, Block.SIZE * 5);
		cent.getChildren().addAll(preview);

		
		//������ʾ��ǰ����
		HBox score = new HBox(10);
		score.setAlignment(Pos.CENTER);
		points = new Label("����  " + scores);
		points.setTextAlignment(TextAlignment.CENTER);
		points.setFont(Font.font(SCREEN_WIDTH * 0.01464));
		score.getChildren().addAll(points);

		//��ʾ���ư�ť��play��start��exit��
		VBox buttons = new VBox(10);
		buttons.setAlignment(Pos.CENTER);
		//��ʼ��������ͣ�����ť�ſ�ʹ��
		p = new Button("��ʼ");
		//δnew������
		p.setDisable(true);
		p.setOnAction(e -> {
			if (!paused) {
				pause();
			} else {
				play();
			}
		});
		r = new Button("����Ϸ");
		r.setOnAction(e -> {
			r.setText("���¿�ʼ");
			p.setDisable(false);
			field.reset();
			//�����ɵķ���ӵ���Ϸ�����
			l = generate();
			field.add(l);
			scores = 200;
			points.setText("���� " + scores);
			play();
		});
		Button ex = new Button("�˳�");
		ex.setOnAction(e -> {
			exit();
		});
		//ȫ�����ư�ť��ӵ�����
		buttons.getChildren().addAll(p, r, ex);

		//�������ò���
		VBox volume = new VBox(10);
		CheckBox mute = new CheckBox("����");
		CheckBox sounde = new CheckBox("��Ч");
		CheckBox musice = new CheckBox("������");
		//�鿴��Ч�Ƿ��п������������������Ϊ����״̬�����������Ч���ر��˾�����Ϊ����״̬����ѡ������ѡ��
		soundWorks = sound.isWorking();
		musice.setOnAction(e -> {
			if (soundWorks) {
				if (!musice.isSelected() && !sounde.isSelected()) {
					mute.setSelected(true);
				} else {
					mute.setSelected(false);
				}
				sound.setMusice(musice.isSelected());
			}
		});
		sounde.setOnAction(e -> {
			if (soundWorks) {
				if (!musice.isSelected() && !sounde.isSelected()) {
					mute.setSelected(true);
				} else {
					mute.setSelected(false);
				}
				sound.setSounde(sounde.isSelected());
			}
		});
		mute.setOnAction(e -> {
			if (soundWorks) {
				musice.setSelected(!mute.isSelected());
				sound.setMusice(!mute.isSelected());
				sounde.setSelected(!mute.isSelected());
				sound.setSounde(!mute.isSelected());
			}
		});
		
		HBox checks = new HBox(10);
		checks.setAlignment(Pos.CENTER);
		checks.getChildren().addAll(mute, sounde, musice);
		volume.getChildren().addAll( checks);
		volume.setAlignment(Pos.CENTER);
		
		//�ѷ�����Ԥ������Ƶ�����ư�ťͬ��ŵ�controls
		controls.getChildren().addAll(score, cent, volume, buttons);
		controls.setAlignment(Pos.CENTER);
		//�����ٶ�
		speed = new Label("Speed 1");
		speed.setFont(Font.font(SCREEN_HEIGHT * 0.04));
		StackPane pane = new StackPane();
		pane.getChildren().add(speed);
		pane.setRotate(90);
		speed.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().addAll(pane,field, controls);
		
		//����һ�෽����ӵ���Ϸ���
		l = generate();
		field.add(l);

		//��Ӽ��̼����¼�
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			switch (e.getCode()) {
			case RIGHT:
				//���������ƶ��жϵ�ǰʱ��ʱ��ͣ״̬���ܲ��������ƶ�
				if (field.canRight(l) && !paused) {
					l.setPosition(l.getPosition().right());
				}
				break;
			case LEFT:
				//ͬ��
				if (field.canLeft(l) && !paused) {
					l.setPosition(l.getPosition().left());
				}
				break;
			case DOWN:
				//�����������di����
				if (field.isReady()) {
					sound.move();
					Down();
				}
				break;
			case UP:
				//��������ϵ���canRotate�����鿴�Ƿ��ܹ�ת�����������rotate����ת��
				if (field.canRotate(l) && !paused) {
					sound.rotate();
					l.Rotate();
				}
				break;
			case ESCAPE:
				if (!paused) {
					pause();
				} else {
					play();
				}
			break;
			default:
				break;
			}
		});

		timer = new AnimationTimer() {
			double sum = 0;
			@Override
			public void handle(long now) {
				long dt = now - then;
				field.requestFocus();
				if (!ps.isFocused()) {
					pause();
				}
				sum += dt;
				if (sum / 1000000000 >= time) {
					sum = 0;
					if (field.isReady()) {
						sound.move();
						Down();
					}
				}
				then = now;
			}
		};

		//����ƶ���Ļ
		root.setOnMousePressed(e -> {
			initx = e.getSceneX();
			inity = e.getSceneY();
		});
		root.setOnMouseDragged(e -> {
			ps.setX(e.getScreenX() - initx);
			ps.setY(e.getScreenY() - inity);
		});
		mute.fire();
		ps.setOnCloseRequest(e -> {
			e.consume();
			exit();
		});
		
		//���ó������ֺ���̨����
		ps.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(30), new Insets(0))));
		root.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(27), new BorderWidths(2))));
		scene.getStylesheets().addAll("play/checkbox.css","play/slider.css");
		ps.setScene(scene);
		ps.setTitle("����˹����");
		ps.show();
	}

	//�õ���һ�鷽�鲢Ԥ��
	public AbstractShape getNext() {
		//7�����ͷ����������һ��
		int type = (int) (Math.random()*6);
		AbstractShape s;
		switch (type) {
		case 0:
			s = new L(init);
			break;
		case 1:
			s = new J(init);
			break;
		case 2:
			s = new I(init);
			break;
		case 3:
			s = new O(init);
			break;
		case 4:
			s = new S(init);
			break;
		case 5:
			s = new Z(init);
			break;
		case 6:
			s = new T(init);
			break;
		default:
			s = new L(init);
		}
		//����任��ɫ
		s.setFill(colors[(int) (Math.random() * 5)]);
		return s;
	}
	//���ɷ���
	public AbstractShape generate() {
		int type = (int) (Math.random()*6);
		AbstractShape s;
		switch (type) {
		case 0:
			s = new L(init);
			break;
		case 1:
			s = new J(init);
			break;
		case 2:
			s = new I(init);
			break;
		case 3:
			s = new O(init);
			break;
		case 4:
			s = new S(init);
			break;
		case 5:
			s = new Z(init);
			break;
		case 6:
			s = new T(init);
			break;
		default:
			s = new L(init);
		}
		s.setFill(colors[(int) (Math.random() * 5)]);
		preview.setShape(getNext().copy());
		return s;
	}

	//ʧ��
	void lose() {
		speed.setText("Speed 1");
		time = 0.5;
		p.setDisable(true);
		r.setText("Start");
		sound.lose();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(ps);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.setContentText("You Lose ! your score is " + scores);
			alert.setOnHidden(e -> {
				field.reset();
				l = generate();
				field.add(l);
				scores = 0;
				points.setText("Score\n" + scores);
			});
			Platform.runLater(alert::showAndWait);
	}

	void initColors() {
		colors[0] = Color.CYAN;
		colors[1] = Color.YELLOW;
		colors[2] = Color.LIGHTGREEN;
		colors[3] = Color.LIGHTBLUE;
		colors[4] = Color.LIGHTPINK;
	}
	//���ݷ��������ٶ�
	public void addScore(int s) {
		scores += s;
		if (scores >= 100) {
			if (scores < 200) {
				time = 0.4;
				speed.setText("Speed 2");
				field.setInvert(true);
			} else if (scores < 400) {
				time = 0.3;
				speed.setText("Speed 3");
				field.setInvert(false);
			} else if (scores < 600) {
				time = 0.2;
				speed.setText("Speed 4");
				field.setInvert(true);
			} else if (scores < 800){
				time = 0.1;
				speed.setText("Speed 5");
				field.setInvert(false);
			}else  {
				field.setInvert(true);
			}
		}
		points.setText("Score\n" + scores);
	}

	//��ͣ����
	void pause() {
		p.setText("Play");
		timer.stop();
		sound.pause();
		paused = true;
		sound.setPauseMusic();
	}
	
	//��ͣ��ʼ
	void play() {
		p.setDisable(false);
		p.setText("Pause");
		then = System.nanoTime();
		timer.start();
		sound.play();
		paused = false;
		sound.setPlayMusic();
	}

	//���º���
	void Down() {
		if (field.canFall(l) && !paused) {
			l.setPosition(l.getPosition().down());
		} else if (!field.canFall(l) && !paused) {
			sound.grounded();
			l.setFill(stock);
			l = generate();
			if (field.isLost())
				lose();
			else
				field.add(l);

			field.checkForRemovableLines(sound, this);
		}
	}
	//�˳���Ϣ��
	void exit() {
		Alert alert = new Alert(AlertType.NONE);
		alert.initOwner(ps);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.setTitle("Exit Confirmation");
		alert.setContentText("Are You Sure?");
		alert.showAndWait();
		if (alert.getResult().equals(ButtonType.YES)) {
			Platform.exit();
		}
	}
}
