function [ label_vector1 ] = test_label_vector()
%TEST_LABEL_VECTOR Summary of this function goes here
%   Detailed explanation goes here

mydir=uigetdir('d:/trainsift','选择一个目录'); %获得选择的文件夹
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);

label_vector1 = [];
number = 0;

for i=3:n  %因为1,2 里面分别是 . .. ,所以要从3开始
 if train_classes(i).isdir   %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)对应的是*.jpg
    class_len = length(pictures);
        
    for j = 3:class_len  %从3开始的原因与上面相同， 处理每个class里面的文件
       number = number +1; 
       %记录图片的label
        label_vector1(number)=i-2;
       %记录图片的label
    end
 end
end
save('test_label_vector','label_vector1');

