需要做的工作
454中reads的处理分两类，并分别保存：
（1）454CT：将454reads中的C转化成T；
（2）454GA：将454reads中的G转化成A；
Reference的处理。
目前给的数据为Watson链数据，需要对应产生Crick链数据，方法为：逆向读取，并取对应的值，A-T，G-C互补
将C转换成T
比对工作
分别将454CT、454GA和Watson链数据、Crick链数据比对
输出
（1）按照Chr输出
（2）按照reads顺序输出
输出格式要求：
Read ID
Ref ID
Mapping strand of ref
Starting position of ref in the alignment
End position of ref in the alignment
Length of read
Length of ref

二， 计算mismatch/match 并且按从大到小顺序输出 所有匹配的read

三，对现在的结果再过滤，给出满足以下条件的reads结果
   1.read>200bp
   2 non-mapped portion<50bp 

