package ac.itcr.game;

import java.util.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{
	public static int camH = 1500;
	public static int roadW = 2000;
	public static int segL = 200;
	public static int trackSize = 3000;
	public static int curveSize = 400/2;
	
	public static float camD = (float) 0.84;
	public static int width = Game.width;
	public static int height = Game.height;
	
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
	//posicion del servidor
	public int posCar1 = 20000;
	
	public int car2;
	public Image car2PNG;
	public int carx2 = 50;
	public int cary2 = 0;
	public float scalecar2 = 1f;
	//posicion del servidor
	public int posCar2 = 60000;
	
	public int car3;
	public Image car3PNG;
	public int carx3 = 100;
	public int cary3 = 0;
	public float scalecar3 = 1f;
	//posicion del servidor
	public int posCar3 = 80000;
	
	
	public Play(int state) {
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		Image carrosPNG = new Image("res/carros.png");
		carros = new SpriteSheet(carrosPNG,42,43);
		
		
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gc.setTargetFrameRate(60);
		
		Vector<Line> lines = new Vector<Line>();
		for(int i=0;i<trackSize;i++) {
			Line line = new Line();
		    line.z = i*segL;
		    
		    if(i>(trackSize*1/4)-curveSize && i<(trackSize*1/4)+curveSize) line.curve=(float) 0.7;
		    
		    if(i>(trackSize*2/4)-curveSize && i<(trackSize*2/4)+curveSize) line.curve=(float) 0.7;
		    
		    if(i>(trackSize*3/4)-curveSize && i<(trackSize*3/4)+curveSize) line.curve=(float) 0.7;
		    
		    if(i>(trackSize*4/4)-curveSize && i<(trackSize*4/4)+curveSize) line.curve=(float) 0.7;
		    
		    
		    
		    lines.add(line);
		    
		}
		int N = lines.size();
		
		
		while(pos>=N*segL) pos-=N*segL;
		while(pos<0)pos+=N*segL;
		
		int startPos = pos/segL;
		float x=0,dx=0;
		
		//draw road
		
		for(int n = startPos;n<startPos+300;n++) {
			Line l = new Line();
			l = lines.get(n%N);
			int t;
			if(n>=N)
				t= N*segL;
			else 
				t=0;
			l.project(playerX - x, camH, pos - t);
			x+=dx;
			dx+=l.curve;
			
			
			
			Color grass;
			if (!((n/3)%2==0))
				grass = new Color(16,200,16);
			else
				grass = new Color(0,154,0);
			Color rumble;
			if (!((n/3)%2==0))
				rumble = new Color(255,255,255);
			else
				rumble = new Color(0,0,0);
			Color road;
			if (!((n/3)%2==0))
				road = new Color(107,107,107);
			else
				road = new Color(105,105,105);
			
			Line p = new Line();
			p = lines.get(Math.floorMod(n-1,N));
			
			grassS = drawQuad(0,(int)p.Y,width,0,(int)l.Y,width);
			g.setColor(grass);
			g.fill(grassS);
			g.draw(grassS);
			
			rumbleS = drawQuad((int)p.X,(int)p.Y,(int)(p.W*1.2),(int)l.X,(int)l.Y,(int)(l.W*1.2));
			g.setColor(rumble);
			g.fill(rumbleS);
			g.draw(rumbleS);
			
			roadS = drawQuad((int)p.X,(int)p.Y,(int)p.W,(int)l.X,(int)l.Y,(int)l.W);
			g.setColor(road);
			g.fill(roadS);
			g.draw(roadS);
		}
		
		
		//Image bg2= new Image("res/img/bg.png");
		//g.drawImage(bg2,0,0);
		//Image bg3= new Image("res/img/bg.png");
		//g.drawImage(bg3,768,0);
		
		//car1PNG = carros.getSubImage(1, 1);
		//car2PNG = carros.getSubImage(1, 3);
		//car3PNG = carros.getSubImage(1, 7);

		
		//car1PNG.draw(carx1, cary1, scalecar1);
		//car1PNG.draw(carx2, cary2, scalecar2);
		//car1PNG.draw(carx3, cary3, scalecar3);
		
		//jugador 1 2 y 3
		carros.getSubImage(1, 1).draw(carx1, cary1, scalecar1);
		carros.getSubImage(1, 3).draw(carx2, cary2, scalecar2);
		carros.getSubImage(1, 5).draw(carx3, cary3, scalecar3);
		
		//jugador principal
		carros.getSubImage(dirrection, carColor).draw(width/2-(3*42/2), height-(43*3),3f);
		
		
		
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		int temp = (int)(velocidad*aceleracion);
		if(input.isKeyDown(Input.KEY_UP)) {
			dirrection = 1;
			pos+=temp;
			if(playerX<1950&&playerX>-1950&&aceleracion<=2) {
				aceleracion+=0.01;
				System.out.print("Aceleracion: " + aceleracion + "\n");
				System.out.print("PosX: " + playerX + "\n");
			}
			System.out.print("Posicion: " + pos + "\n");
		}
		if(input.isKeyDown(Input.KEY_DOWN)) {
			dirrection = 1;
			pos-=200;
		}
		if((!input.isKeyDown(Input.KEY_UP)&&aceleracion!=0)||(playerX>1950||playerX<-1950)) {
			dirrection = 1;
			System.out.println("deteniendo!!!");
			pos+=temp;
			if(aceleracion>0) {
				aceleracion-=0.13;
				System.out.print("Aceleracion: " + aceleracion + "\n");
				System.out.print("PosX: " + playerX + "\n");
			}
			if(aceleracion<0) {
				aceleracion=0;
			}
			System.out.print("Posicion: " + pos + "\n");
		}
		
		if(input.isKeyDown(Input.KEY_RIGHT)&&playerX<2000) {
			playerX+=50;
			dirrection = 2;
			//carx1-=30*scalecar1;
		}
			
		if(input.isKeyDown(Input.KEY_LEFT)&&playerX>-2000) {
			playerX-=50;
			dirrection = 0;
			//carx1+=30*scalecar1;
		}
		if(input.isKeyDown(Input.KEY_SPACE)) {
			//Shape disparo = new Shape();
		}
		//pruebas de carros
		if(input.isKeyDown(Input.KEY_W)) {
			posCar1+=100;
			
			//car1PNG.draw(carx1, cary1, scalecar1);
		}
		if(input.isKeyDown(Input.KEY_S)) {
			cary1+=20;
			scalecar1+=0.2;
			
			//car1PNG.draw(carx1, cary1, scalecar1);
		}
		//imprimir carro 1
		if(Math.abs(pos-posCar1)<7000) {
			if(pos<posCar1) {
				System.out.println("PosCar1: " + posCar1);
				cary1=350+350*(7000-Math.abs(pos-posCar1))/7000;
				scalecar1=3.2f*(7000-Math.abs(pos-posCar1))/6000;
				carx1=width/2;
			}
			if(posCar1<pos) {
				cary1=0;
				carx1=0;
				scalecar1=1f;
			}
		}
		//imprimir carro 2
		if(Math.abs(pos-posCar2)<7000) {
			if(pos<posCar2) {
				System.out.println("PosCar2: " + posCar2);
				cary2=350+350*(7000-Math.abs(pos-posCar2))/7000;
				scalecar2=3.2f*(7000-Math.abs(pos-posCar2))/6000;
				carx2=width/2;
			}
			if(posCar2<pos) {
				cary2=0;
				carx2=0;
				scalecar2=1f;
			}
		}
		//imprimir carro 3
		if(Math.abs(pos-posCar3)<7000) {
			if(pos<posCar3) {
				System.out.println("PosCar3: " + posCar3);
				cary3=350+350*(7000-Math.abs(pos-posCar3))/7000;
				scalecar3=3.2f*(7000-Math.abs(pos-posCar3))/6000;
				carx3=width/2;
			}
			if(posCar3<pos) {
				cary3=0;
				carx3=0;
				scalecar3 = 1f;
			}
		}
		
			
	}

	@Override
	public int getID() {
		
		return 1;
	}
	
	public class Line {

		float x,y,z; //3d center of line
		float X,Y,W; //screen coord
		float curve,scale;
		double spriteX;
		Image sprite;

		 Line(){
			 curve=x=y=z=0;
		 }
		 void project(float camX,int camY,int camZ){
		    scale = camD/(z-camZ);
		    X = (1 + scale*(x - camX)) * width/2;
		    Y = (1 - scale*(y - camY)) * height/2;
		    W = scale * roadW  * width/2;
		 }

		 
		 
	
		
	}
	
	public Polygon drawQuad(int x1,int y1,int w1,int x2,int y2,int w2) {
		Polygon shape = new Polygon();
	    shape.addPoint(x1-w1,y1);
	    shape.addPoint(x2-w2,y2);
	    shape.addPoint(x2+w2,y2);
	    shape.addPoint(x1+w1,y1);
	    return shape;
	}

}
