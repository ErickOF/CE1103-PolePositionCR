package ce.itcr.game;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Play extends BasicGameState {
	public static int camH = 1500;
	public static int roadW = 2000;
	public static int segL = 200;
	public static int trackSize = 3000;
	public static int curveSize = 400 / 2;

	public static float camD = (float) 0.84;
	public static int width = 1024;
	public static int height = 700;

	public Shape grassS;
	public Shape rumbleS;
	public Shape roadS;

	public int pos = 0;
	public int playerX = 0;

	public double aceleracion = 0;
	public static int velocidad = 100;

	public SpriteSheet carros;
	public int dirrection = 1;
	public int carColor = 0;

	public int car1;
	public Image car1PNG;
	public int carx1 = 0;
	public int cary1 = 0;
	public float scalecar1 = 1f;
	// posicion del servidor
	public int posCar1 = 0;

	public int car2;
	public Image car2PNG;
	public int carx2 = 50;
	public int cary2 = 0;
	public float scalecar2 = 1f;
	// posicion del servidor
	public int posCar2 = 60000;

	public int car3;
	public Image car3PNG;
	public int carx3 = 100;
	public int cary3 = 0;
	public float scalecar3 = 1f;
	// posicion del servidor
	public int posCar3 = 80000;

	public int contador = 0;

	Vector<Line> lines;

	int N;

	public Play(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		Image carrosPNG = new Image("res/img/carros.png");
		carros = new SpriteSheet(carrosPNG, 42, 43);

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
		N = lines.size();

		new Thread(new Runnable() {
			int curva = 0;

			public void run() {
				while (posCar1 <= 600000) {
					posCar1 += (velocidad * 3);

					int indexLines = posCar1 / 200 % N;
					Line line = lines.get(indexLines);

					cary1 = (int) line.Y;
					// scalecar1=3.2f*(7000-Math.abs(pos-posCar1))/6000;
					if (line.curve == 0) {
						curva = 0;
					} else {
						curva = (int) line.W / 2;
					}

					carx1 = (int) (line.X - curva);

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		gc.setTargetFrameRate(60);

		while (pos >= N * segL)
			pos -= N * segL;
		while (pos < 0)
			pos += N * segL;

		int startPos = pos / segL;
		float x = 0, dx = 0;

		// draw road
		for (int n = startPos; n < startPos + 300; n++) {
			Line l = new Line();
			l = lines.get(n % N);

			int t;
			if (n >= N)
				t = N * segL;
			else
				t = 0;
			l.project(playerX - x, camH, pos - t);
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
			p = lines.get(Math.floorMod(n - 1, N));

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

		// jugador 1 2 y 3
		carros.getSubImage(1, 1).draw(carx1, cary1, scalecar1);
		// carros.getSubImage(1, 3).draw(carx2, cary2, scalecar2);
		// carros.getSubImage(1, 5).draw(carx3, cary3, scalecar3);

		// jugador principal
		carros.getSubImage(dirrection, carColor).draw(width / 2 - (3 * 42 / 2),
				height - (43 * 3), 3f);

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		int temp = (int) (velocidad * aceleracion);
		if (input.isKeyDown(Input.KEY_UP)) {
			dirrection = 1;
			pos += temp;
			if (playerX < 1950 && playerX > -1950 && aceleracion <= 2) {
				aceleracion += 0.01;

			}

		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			dirrection = 1;
			pos -= 200;
		}
		if ((!input.isKeyDown(Input.KEY_UP) && aceleracion != 0)
				|| (playerX > 1950 || playerX < -1950)) {
			dirrection = 1;

			pos += temp;
			if (aceleracion > 0) {
				aceleracion -= 0.13;

			}
			if (aceleracion < 0) {
				aceleracion = 0;
			}
		}

		if (input.isKeyDown(Input.KEY_RIGHT) && playerX < 2000) {
			playerX += 50;
			dirrection = 2;
			// carx1-=30*scalecar1;
		}

		if (input.isKeyDown(Input.KEY_LEFT) && playerX > -2000) {
			playerX -= 50;
			dirrection = 0;
			// carx1+=30*scalecar1;
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			// Shape disparo = new Shape();
		}

		if (pos < posCar1) {
			// System.out.println("PosCar1: " + posCar1/segL);
			float factor = (300 - (float) (posCar1 - pos) / segL) / (300);
			scalecar1 = 2f * factor;

		}
		if (posCar1 < pos) {
			cary1 = 0;
			carx1 = 0;
			scalecar1 = 3f;
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
			System.out.println("w " + W);
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