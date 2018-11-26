package Step2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/*
 * 测试setAccessible方法，可以通过将它设置为true--setAccessible(true) 来访问private属性和函数。
 * 而且可以提高程序的执行效率，因为减少了安全检查。
 */
public class reflectionTest3 {
	
	public static void main(String[] args){
		try {
			String path = "Step2.User3"; 
			Class clazz = Class.forName(path);
			
			//Method method = clazz.getMethod("setName",String.class);
			//getMethod只能获取public的方法，private的方法需要使用getDeclaredMethod来获取，并且设置setAccessible(true)才可以调用访问。
			//参数属性也是一样。
			Method method = clazz.getDeclaredMethod("setName", String.class);
			method.setAccessible(true);
			
			//Constructor strut = clazz.getConstructor(String.class,Integer.class);
			//getConstructor只能获取public的构造方法
			Constructor strut = clazz.getDeclaredConstructor(String.class,Integer.class);
			strut.setAccessible(true);
			User3 user = (User3)strut.newInstance("bit4",19);
			//调用自定义构造器的方法
			Object x = method.invoke(user,"比特");//第一个参数是类的对象。第二参数是函数的参数
			System.out.println(user.getName());
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}

class User3{
	
	private Integer age;
	private String name;
	
	private User3() {}
    
	private User3(String name,Integer age){ //构造函数，初始化时执行
    	this.age = age;
    	this.name = name;
    }
    

	private Integer getAge() {
		return age;
	}

	private void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
    	this.name = name;
    }
}