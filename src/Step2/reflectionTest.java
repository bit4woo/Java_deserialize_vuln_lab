package Step2;

import java.lang.reflect.Method;

public class reflectionTest {
	
	public static void main(String[] args){
		try {

			//Class获取类的方法一:实例对象的getClass()方法;
			User testObject = new User("zhangshan",19);
			Class Method1Class = testObject.getClass();
			
			//Class获取类的方法二:类的.class(最安全/性能最好)属性;有点类似python的getattr()。java中每个类型都有class 属性.
			Class Method2Class = User.class;
			
			//Class对象的获取方法三:运用Class.forName(String className)动态加载类,className需要是类的全限定名(最常用).
			//这种方法也最容易理解，通过类名(jar包中的完整namespace)就可以调用其中的方法，也最符合我们需要的使用场景.
			//j2eeScan burp 插件就使用了这种反射机制。
			String path = "Step2.User"; 
			Class Method3Class = Class.forName(path);
			
			
			
			Method[] methods = Method3Class.getMethods();
			//Method[] methods = Method2Class.getMethods();
			//Method[] methods = Method3Class.getMethods();
			
			//通过类的class属性获取对应的Class类的对象，通过这个Class类的对象获取test类中的方法集合

			/* String name = Method3Class.getName();
			 * int modifiers = Method3Class.getModifiers();
			 * .....还有很多方法
			 * 也就是说，对于一个任意的可以访问到的类，我们都能够通过以上这些方法来知道它的所有的方法和属性；
			 * 知道了它的方法和属性，就可以调用这些方法和属性。
			 */
			
			//调用User类中的方法
			
			for(Method method : methods){
			    if(method.getName().equals("getName")) {
			    	System.out.println("method = " + method.getName());
			    	
			    	Class[] parameterTypes = method.getParameterTypes();//获取方法的参数
			    	Class returnType = method.getReturnType();//获取方法的返回类型
			    	try {
			    		User user = (User)Method3Class.newInstance();
			    		Object x = method.invoke(user);//user.getName();
			    		//Object x = method.invoke(new test(1), 666);
			    		//new关键字能调用任何构造方法,newInstance()只能调用无参构造方法。但反射的场景中是不应该有机会使用new关键词的。
			    		System.out.println(x);
			    		
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }
			}
			
			
			
			Method method = Method3Class.getMethod("setName",String.class);
			User user1 = (User)Method3Class.getConstructor(String.class,Integer.class).newInstance("lisi",19);
			//调用自定义构造器的方法
			Object x = method.invoke(user1,"李四");//第一个参数是类的对象。第二参数是函数的参数
			System.out.println(user1.getName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}

class User{
	private Integer age;
	private String name;
	
    public User() {}
    
    public User(String name,Integer age){ //构造函数，初始化时执行
    	this.age = age;
    	this.name = name;
    }
    

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}
	
    public void setName(String name) {
    	this.name = name;
    }
}