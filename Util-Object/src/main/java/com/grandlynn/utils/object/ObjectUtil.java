/*
 * MIT License
 *
 * Copyright (c) 2017 JinhuaiLee
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.grandlynn.utils.object;

import java.lang.reflect.Field;

/**
 * 对象操作工具类
 *
 * @author jinhuailee
 * @date 2017/11/29 20:21
 */
public class ObjectUtil {
    /**
     * 将sourceObj的属性拷贝到targetObj
     *
     * @param sourceObj
     * @param targetObj
     * @param clazz     从哪一个类开始(比如sourceObj对象层级为:Object->User->ChineseUser->ChineseMan->ChineseChongQingMan)
     *                  <p>
     *                  如果需要从ChineseUser开始复制，clazz就指定为ChineseUser.class
     */

    public static void cpoyObjAttr(Object sourceObj, Object targetObj, Class<?> clazz) throws Exception {
        if (sourceObj == null || targetObj == null) {
            throw new Exception("源对象和目标对象不能为null");
        }

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object sourceValue = fields[i].get(sourceObj);
            fields[i].set(targetObj, sourceValue);
        }
        if (clazz.getSuperclass() == Object.class) {
            return;
        }
        cpoyObjAttr(sourceObj, targetObj, clazz.getSuperclass());
    }
}
