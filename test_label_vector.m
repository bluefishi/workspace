function [ label_vector1 ] = test_label_vector()
%TEST_LABEL_VECTOR Summary of this function goes here
%   Detailed explanation goes here

mydir=uigetdir('d:/trainsift','ѡ��һ��Ŀ¼'); %���ѡ����ļ���
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);

label_vector1 = [];
number = 0;

for i=3:n  %��Ϊ1,2 ����ֱ��� . .. ,����Ҫ��3��ʼ
 if train_classes(i).isdir   %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)��Ӧ����*.jpg
    class_len = length(pictures);
        
    for j = 3:class_len  %��3��ʼ��ԭ����������ͬ�� ����ÿ��class������ļ�
       number = number +1; 
       %��¼ͼƬ��label
        label_vector1(number)=i-2;
       %��¼ͼƬ��label
    end
 end
end
save('test_label_vector','label_vector1');

