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
	public static int roadW = 2000;
	public static int segL = 200;
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
	public int carx1 = 512;
	public int cary1 = 600;
	public float scalecar1 = 3f;

	public int car2;
	public int dirrectioncar2 = 1;
	public int carColorcar2 = 3;
	public float scalecar2 = 3f;

	public int car3;
	public int dirrectioncar3 = 1;
	public int carColorcar3 = 5;
	public float scalecar3 = 3f;

	public Image arbolesPNG;

	public Play(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		Image carrosPNG = new Image("res/img/carros.png");
		carros = new SpriteSheet(carrosPNG, 42, 43);
		car1PNG = carros.getSubImage(1, 1);

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		gc.setTargetFrameRate(60);

		Vector<Line> lines = new Vector<Line>();
		for (int i = 0; i < 3000; i++) {
			Line line = new Line();
			line.z = i * segL;

			if (i > 550 && i < 950)
				line.curve = (float) -0.7;

			if (i > 1300 && i < 1700)
				line.curve = (float) 0.7;

			if (i > 2050 && i < 2450)
				line.curve = (float) 0.7;

			if (i > 2800 && i < 200)
				line.curve = (float) 0.7;

			// if (Math.floorMod(i, 20)==0) {
			line.spriteX = -2.5;
			line.sprite = arbolesPNG;
			// }

			lines.add(line);

		}
		int N = lines.size();

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
			l.project(playerX - x, 1500, pos - t);
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

		Image bg2 = new Image("res/img/bg.png");
		g.drawImage(bg2, 0, 0);
		Image bg3 = new Image("res/img/bg.png");
		g.drawImage(bg3, 768, 0);

		car1PNG.draw(carx1, cary1, scalecar1);

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
				System.out.print("Aceleracion: " + aceleracion + "\n");
				System.out.print("PosX: " + playerX + "\n");
			}
			System.out.print("Posicion: " + pos + "\n");
		}

		if ((!input.isKeyDown(Input.KEY_UP) && aceleracion != 0)
				|| (playerX > 1950 || playerX < -1950)) {
			dirrection = 1;
			System.out.println("deteniendo!!!");
			pos += temp;
			if (aceleracion > 0) {
				aceleracion -= 0.13;
				System.out.print("Aceleracion: " + aceleracion + "\n");
				System.out.print("PosX: " + playerX + "\n");
			}
			if (aceleracion < 0) {
				aceleracion = 0;
			}
			System.out.print("Posicion: " + pos + "\n");
		}
		if (input.isKeyDown(Input.KEY_RIGHT) && playerX < 2000) {
			playerX += 50;
			dirrection = 2;
			carx1 -= 50 / scalecar1;
		}

		if (input.isKeyDown(Input.KEY_LEFT) && playerX > -2000) {
			playerX -= 50;
			dirrection = 0;
			carx1 += 50 / scalecar1;
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			// Shape disparo = new Shape();
		}

		if (input.isKeyDown(Input.KEY_W)) {
			cary1 -= 20;
			scalecar1 -= 0.2;

			// car1PNG.draw(carx1, cary1, scalecar1);
		}
		if (input.isKeyDown(Input.KEY_S)) {
			cary1 += 20;
			scalecar1 += 0.2;

			// car1PNG.draw(carx1, cary1, scalecar1);
		}

	}

	@Override
	public int getID() {

		return 1;
	}

	public class Line {

		float x, y, z; // 3d center of line
		float X, Y, W; // screen coord
		float curve, scale, clip;
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
