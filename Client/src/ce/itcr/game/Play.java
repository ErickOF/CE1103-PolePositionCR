package ce.itcr.game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ce.itcr.game.sprites.Hueco;
import ce.itcr.game.sprites.Jugador;
import ce.itcr.game.sprites.Poder;
import ce.itcr.game.sprites.Sprite;
import ce.itcr.game.sprites.Turbo;
import ce.itcr.game.sprites.Vida;

public class Play extends BasicGameState {
	private final float gameoverY = 700;
	private final float gameoverX = 1024;
	public static Integer camH = 1500;
	public static Integer roadW = 2000;
	public static Integer segL = 200;
	public static Integer trackSize = 3000;
	public static Integer curveSize = 400 / 2;

	public static Float camD = (float) 0.84;
	public static Integer width = 1024;
	public static Integer height = 700;

	public Shape grassS;
	public Shape rumbleS;
	public Shape roadS;

	public SpriteSheet carrosSpriteSheet;
	public SpriteSheet poderesSpriteSheet;
	public int dirrection = 1;
	public int carColor = 0;

	Vector<Line> lines;
	int linesN;

	Sprite[] poderes;

	Jugador jugadorPrincipal;
	Jugador[] jugadoresSec;
	private ArrayList<Sprite> sprites;

	public Play(int state) {

	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		Image carrosPNG = new Image("res/img/carros.png");
		carrosSpriteSheet = new SpriteSheet(carrosPNG, 42, 43);

		lines = new Vector<Line>();
		for (int i = 0; i < trackSize; i++) {
			Line line = new Line();
			line.z = i * segL;

			if (i > (trackSize * 1 / 4) - curveSize
					&& i < (trackSize * 1 / 4) + curveSize)
				line.curve = (float) 0.7;

			if (i > (trackSize * 2 / 4) - curveSize
					&& i < (trackSize * 2 / 4) + curveSize)
				line.curve = (float) 0.7;

			if (i > (trackSize * 3 / 4) - curveSize
					&& i < (trackSize * 3 / 4) + curveSize)
				line.curve = (float) 0.7;

			if (i > (trackSize * 4 / 4) - curveSize
					&& i < (trackSize * 4 / 4) + curveSize)
				line.curve = (float) 0.7;

			lines.add(line);

		}
		linesN = lines.size();

		Image poderesPng = new Image("res/img/carros.png");
		poderesSpriteSheet = new SpriteSheet(poderesPng, 42, 43);

		// se crean los poderes
		poderes = new Sprite[] {
				new Hueco(poderesSpriteSheet.getSubImage(1, 6), 0, false),
				new Turbo(poderesSpriteSheet.getSubImage(1, 7), 0, false),
				new Vida(poderesSpriteSheet.getSubImage(1, 8), 0, false),

		};

		// se crea el jugador principal
		jugadorPrincipal = new Jugador(carrosSpriteSheet.getSubImage(
				dirrection, carColor), 0, true);
		jugadorPrincipal.setCoordY((int) (height - jugadorPrincipal.icon
				.getHeight() * jugadorPrincipal.scale));
		// jugadorPrincipal.setCoordX((int)
		// (width-jugadorPrincipal.icon.getWidth()*jugadorPrincipal.scale/2));
		jugadorPrincipal.setCoordX((int) (width / 2 - jugadorPrincipal.icon
				.getWidth() * jugadorPrincipal.scale));

		// se crean los jugadores secundarios
		jugadoresSec = new Jugador[] {
				new Jugador(carrosSpriteSheet.getSubImage(1, 1), 0, true),
				new Jugador(carrosSpriteSheet.getSubImage(1, 2), 0, false),
				new Jugador(carrosSpriteSheet.getSubImage(1, 3), 0, false) };

		sprites = new ArrayList<Sprite>();
		for (Sprite poder : poderes) {
			sprites.add(poder);
		}
		for (Jugador jugador : jugadoresSec) {
			sprites.add(jugador);
		}
		escucharServidor();

	}

	public void escucharServidor() {
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					drawSprites();
					checkPowersAndCollisions();

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

	private void drawSprites() {
		for (Sprite sprite : sprites) {

			if (sprite.activo) {
				Integer curva = 0;
				Integer lineIndex = sprite.getPosition() / 200 % linesN;
				Line line = lines.get(lineIndex);

				if (sprite.getClass() == Jugador.class) {
					Jugador jugador = (Jugador) sprite;
					jugador.setAceleracion(1f);
					sprite.setPosition(sprite.getPosition()
							+ jugador.getSpeed());

				}

				sprite.setCoordY((int) line.Y);

				if (line.curve == 0) {
					curva = 0;
				} else {
					curva = (int) line.W / 2;
				}
				sprite.setCoordX((int) (line.X - curva));
			}
		}

	}

	private void checkPowersAndCollisions() {
		for (Sprite poder : poderes) {
			if (poder.activo) {
				if (poder.getPosition() == jugadorPrincipal.getPosition()) {
					((Poder) poder).implementar(jugadorPrincipal);
				}
			}
		}
		for (Jugador jugador : jugadoresSec) {
			if (jugador.activo) {
				// System.out.println("eneY "+jugador.getCoordY()+
				// " yo Y "+jugadorPrincipal.getCoordY());
				// System.out.println("eneX "+jugador.getCoordX()+
				// " yo X "+jugadorPrincipal.getCoordX());
				jugador.collided(jugadorPrincipal);
			}
		}

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		gc.setTargetFrameRate(60);

		while (jugadorPrincipal.getPosition() >= linesN * segL)
			jugadorPrincipal.setPosition(jugadorPrincipal.getPosition()
					- linesN * segL);
		while (jugadorPrincipal.getPosition() < 0)
			jugadorPrincipal.setPosition(jugadorPrincipal.getPosition()
					+ linesN * segL);

		int startPos = jugadorPrincipal.getPosition() / segL;
		float x = 0, dx = 0;

		// draw road

		for (int n = startPos; n < startPos + 300; n++) {
			Line l = new Line();
			l = lines.get(n % linesN);

			int t;
			if (n >= linesN)
				t = linesN * segL;
			else
				t = 0;
			l.project(jugadorPrincipal.getCoordX() - x, camH,
					jugadorPrincipal.getPosition() - t);
			x += dx;
			dx += l.curve;

			Color grass;
			if (!((n / 3) % 2 == 0))
				grass = new Color(16, 200, 16);
			else
				grass = new Color(0, 154, 0);
			Color rumble;
			if (!((n / 3) % 2 == 0))
				rumble = new Color(255, 255, 255);
			else
				rumble = new Color(0, 0, 0);
			Color road;
			if (!((n / 3) % 2 == 0))
				road = new Color(107, 107, 107);
			else
				road = new Color(105, 105, 105);

			Line p = new Line();

			p = lines.get(Math.floorMod(n - 1, linesN));

			grassS = drawQuad(0, (int) p.Y, width, 0, (int) l.Y, width);
			g.setColor(grass);
			g.fill(grassS);
			g.draw(grassS);

			rumbleS = drawQuad((int) p.X, (int) p.Y, (int) (p.W * 1.2),
					(int) l.X, (int) l.Y, (int) (l.W * 1.2));
			g.setColor(rumble);
			g.fill(rumbleS);
			g.draw(rumbleS);

			roadS = drawQuad((int) p.X, (int) p.Y, (int) p.W, (int) l.X,
					(int) l.Y, (int) l.W);
			g.setColor(road);
			g.fill(roadS);
			g.draw(roadS);

		}

		// jugador principal
		jugadorPrincipal.icon = carrosSpriteSheet.getSubImage(dirrection,
				carColor);
		jugadorPrincipal.icon.draw(width / 2 - (3 * 42 / 2), height
				- jugadorPrincipal.icon.getHeight() * jugadorPrincipal.scale,
				jugadorPrincipal.scale);

		// dibuja los poderes
		for (Sprite poder : poderes) {
			poder.icon.draw(poder.getCoordX(), poder.getCoordY(), 1f);
		}

		// dibuja los jugadores
		for (Jugador jugadorSec : jugadoresSec) {
			if (jugadorSec.activo) {
				jugadorSec.icon.draw(jugadorSec.getCoordX(),
						jugadorSec.getCoordY(), jugadorSec.scale);
			}
		}

		Image bg = new Image("res/img/bg.png");
		Image bg2 = new Image("res/img/bg.png");
		bg.draw(0, 0);
		bg2.draw(766, 0);
		Image go = new Image("res/img/gameover.png");
		go.draw(gameoverX, gameoverY);

		Font font = new Font("Verdana", Font.BOLD, 30);
		TrueTypeFont trueTypeFont = new TrueTypeFont(font, true);
		trueTypeFont.drawString(20.0f, 630.0f,
				String.valueOf(jugadorPrincipal.getSpeed() / 3) + " KM/H",
				Color.black);

		trueTypeFont.drawString(870.0f, 630.0f,
				"Vidas " + String.valueOf(jugadorPrincipal.vidas), Color.black);

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		// System.out.println(jugadorPrincipal.getAceleracion());

		int temp = jugadorPrincipal.getSpeed();
		if (input.isKeyDown(Input.KEY_UP)) {
			dirrection = 1;
			jugadorPrincipal.setPosition(jugadorPrincipal.getPosition() + temp);
			if (jugadorPrincipal.getCoordX() < 1950
					&& jugadorPrincipal.getCoordX() > -1950
					&& jugadorPrincipal.getAceleracion() <= 2) {
				jugadorPrincipal.increaseAceleracion(0.03f);

			}

		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			dirrection = 1;
			jugadorPrincipal.setPosition(jugadorPrincipal.getPosition() - 200);
		}
		if ((!input.isKeyDown(Input.KEY_UP) && jugadorPrincipal
				.getAceleracion() != 0)
				|| (jugadorPrincipal.getCoordX() > 1950 || jugadorPrincipal
						.getCoordX() < -1950)) {
			dirrection = 1;

			jugadorPrincipal.setPosition(jugadorPrincipal.getPosition() + temp);
			if (jugadorPrincipal.getAceleracion() > 0) {
				jugadorPrincipal.increaseAceleracion(-0.03f);

			}
			if (jugadorPrincipal.getAceleracion() < 0) {
				jugadorPrincipal.setAceleracion(0f);
			}
		}

		if (input.isKeyDown(Input.KEY_RIGHT)
				&& jugadorPrincipal.getCoordX() < 2000) {
			jugadorPrincipal.setCoordX(jugadorPrincipal.getCoordX() + 50);
			dirrection = 2;
			// carx1-=30*scalecar1;
		}

		if (input.isKeyDown(Input.KEY_LEFT)
				&& jugadorPrincipal.getCoordX() > -2000) {
			jugadorPrincipal.setCoordX(jugadorPrincipal.getCoordX() - 50);
			dirrection = 0;
			// carx1+=30*scalecar1;
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			// Shape disparo = new Shape();
		}

		checkPowersAndCollisions();

		for (Jugador jugador : jugadoresSec) {
			if (jugadorPrincipal.getPosition() < jugador.getPosition()) {

				float factor = (300 - (float) (jugador.getPosition() - jugadorPrincipal
						.getPosition()) / segL) / (300);
				jugador.scale = 2f * factor;

			}
			if (Integer.compare(jugador.getPosition(),
					jugadorPrincipal.getPosition()) < 0) {
				jugador.setCoordY(0);
				jugador.setCoordX(0);
				jugador.scale = 2f;
			}
		}
		for (Sprite poder : poderes) {
			if (jugadorPrincipal.getPosition() < poder.getPosition()
					&& Math.abs(poder.getPosition()
							- jugadorPrincipal.getPosition()) < 7000) {

				float factor = (300 - (float) (poder.getPosition() - jugadorPrincipal
						.getPosition()) / segL) / (300);
				poder.scale = 1f * factor;
				// poder.setCoordX(width/2);
				System.out.println("/nPosicion poder: " + poder.getPosition());
				System.out.println("/nPosicion jugador: "
						+ jugadorPrincipal.getPosition());

			} else {
				// if(Integer.compare(poder.getPosition(),jugadorPrincipal.getPosition())<0)
				// {
				poder.setCoordY(0);
				poder.setCoordX(0);
				poder.scale = 1f;
			}
			System.out.println("Poder scale: " + poder.scale);
		}

		if (jugadorPrincipal.vidas <= 0) {
			jugadorPrincipal.vidas = 0;
			// gameoverX=0;
			// gameoverY=0;
			while (true) {
			}
		}
	}

	@Override
	public int getID() {
		return 1;
	}

	public class Line {
		float x, y, z; // 3d center of line
		float X, Y, W; // screen coord
		float curve, scale;
		double spriteX;
		Image sprite;

		Line() {
			curve = x = y = z = 0;
		}

		void project(float camX, int camY, int camZ) {
			scale = camD / (z - camZ);
			X = (1 + scale * (x - camX)) * width / 2;
			Y = (1 - scale * (y - camY)) * height / 2;
			W = scale * roadW * width / 2;
		}
	}

	public Polygon drawQuad(int x1, int y1, int w1, int x2, int y2, int w2) {
		Polygon shape = new Polygon();
		shape.addPoint(x1 - w1, y1);
		shape.addPoint(x2 - w2, y2);
		shape.addPoint(x2 + w2, y2);
		shape.addPoint(x1 + w1, y1);
		return shape;
	}

}
