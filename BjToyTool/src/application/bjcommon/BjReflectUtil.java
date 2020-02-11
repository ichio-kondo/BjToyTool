package application.bjcommon;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BjReflectUtil {


	/**
	 * reflectでObjectの中からパラメータのフィールド名で値を取得するメソッド。
	 * @param obj
	 * @param Map<String,Object> フィールド名、値
	 */
	public static Map<String,Object> getSuperClassFildForMap(Object obj , List<String> filedNameList) {

		Map<String,Object> map = new HashMap<String,Object>();

		if (filedNameList == null) return null;
        for(String name : filedNameList){
        	Object o = BjReflectUtil.getSuperClassFild(obj, name);
        	if (o==null) continue;
        	map.put(name,o);
        }
		return map;
	}



	/**
	 * パラメータのObject内のフィールドを親クラスまで検索し値を取得するメソッド
	 * フィールドが見つからない場合はNULLを返します。
	 * @param obj 取得したいフィールドを持つObject
	 * @param name 取得値のフィールド名
	 * @return フィールドの値　取得できない場合はNULL
	 */
	public static Object getSuperClassFild(Object obj, String name) {

		if (obj == null || name == null) {
			return null;
		}

		Class<? extends Object> c = obj.getClass();
		Object result = null;
		Field f = null;
		while (c != null) {
			try {
				f = c.getDeclaredField(name);
				f.setAccessible(true);
				result = f.get(obj);
				return result;
			} catch (NoSuchFieldException e) {
				c = c.getSuperclass();
			} catch (IllegalArgumentException | IllegalAccessException eI) {
				eI.printStackTrace();
				return null;
			}
		}
		return null;


	}
	/**
	 * フィールドの値取得 注意＞親クラスまでは見に行かない
	 * @param obj 取得した値が格納されているObject
	 * @param name 取得したいフィールド名。
	 * @return 値 取得できない場合はNULLを返します。
	 */
	public static Object getField(Object obj, String name ) {

		if (obj == null || name == null) {
			return null;
		}
		Field f = null;
		Object result = null;
		try {

			f = obj.getClass().getDeclaredField(name);
			f.setAccessible(true);
			result = f.get(obj);
		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		}
		return result;
	}


	/**
	 * フィールドの値を設定します。<br />
	 * <注意＞SET側のクラス型とパラメータの値のクラス型はあっている事が前提。
	 *        使う側で担保する事。
	 *
	 * @param obj
	 *            設定対象オブジェクト
	 * @param name
	 *            設定対象フィールド名
	 * @param value
	 *            設定する値
	 */
	public static void put(Object obj, String name, Object value) {
		Field f = null;
		try {
			f = obj.getClass().getDeclaredField(name);
			f.setAccessible(true);
			f.set(obj, value);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Object内にjava.util.Listのfieldがあるかチェック。ある場合該当Listをreturn
	 * @param o
	 * @return List<List<?　extends Object>>
	 */

	public static List<List<? extends Object>> getChildList(Object o) {
		try {
			List<List<? extends Object>> returnList = null;
			for (Field field : o.getClass().getDeclaredFields()) {
		           field.setAccessible(true);
		           //int modifier = field.getModifiers();
		           Class<?> type = field.getType();
		           //String name = field.getName();
		           //Object value = field.get(o);
		           //System.out.println(
		              //type.getTypeName() + " " + type.getClass() + " "+ name + " = " + value);
		           if (type.equals(List.class)) {

		        	   if(returnList == null) {
		        		   returnList = new ArrayList<List<?>>();
		        	   }
		        	   List<? extends Object>value =( List<?> )field.get(o);
		        	   returnList.add( value );
		           }
			}
			 return returnList;
		}catch( IllegalAccessException e) {
			return null;
		}
	}

	/**
	 * 指定したメソッド名を実行し値を取得するメソッド
	 * @param o
	 * @param methodName
	 * @return　methodNameで実行されたReturn値　取得できない場合はNULLが返ります。
	 */
	public static Object getMethod(Object o , String methodName) {

		try {
			Method method = o.getClass().getDeclaredMethod(methodName);
			method.setAccessible(true);
			return method.invoke(o);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch ( NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 指定したメソッド名を実行し値を取得するメソッド(第一引数のみ対応）
	 * @param o
	 * @param methodName
	 * @return　methodNameで実行されたReturn値取得できない場合はNULLが返ります。
	 */
	public static Object getMethod(Object o , String methodName, Object param) {

		try {
			Method method = null;
			if (param == null) {
				 method = o.getClass().getDeclaredMethod(methodName);
				 method.setAccessible(true);
					return method.invoke(o);
			} else {

				method = o.getClass().getDeclaredMethod(methodName,param.getClass());
				method.setAccessible(true);
				return method.invoke(o,param);
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch ( NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <利用注意＞
	 * ・BjFormのフィールドにReflectionでSETするメソッド
	 * 尚、Bjでは、Formのfiledは以下に変換される事が前提のメソッド。
	 * 汎用的な利用はできません。
	 *
	 * ・SETするBjFormのFiledはString型、BigDecimal型、java.util.date型の３種類限定。
	 *   ※Date型にSETする場合は、Date型→Date型のみ対応。
	 *    あからじめ、Date型に変換し、パラメータに指定する事。
	 *
	 * @param obj
	 * @param name
	 * @param value
	 */
	public static BjExcelEntity bjFormFiledSet(BjExcelEntity bjForm, String name, Object value) {
		Field f = null;
		try {
			f = bjForm.getClass().getDeclaredField(name);
			f.setAccessible(true);
			//fieldの型を確認。
			Class<?> clazz = f.getType();
			if (Modifier.isFinal(f.getModifiers())) {
				return bjForm;
			}

			if (clazz == String.class) {
				f.set(bjForm, value.toString());
			} else if (clazz == Date.class) {
				//date型の場合はパラメータはDate型の事が前提
				f.set(bjForm, value);
			} else if (clazz == boolean.class || clazz == Boolean.class) {
				f.set(bjForm, value);
			} else if (clazz == BigDecimal.class) {
				f.set(bjForm, new BigDecimal(value.toString()));
			}
			return bjForm;

		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
