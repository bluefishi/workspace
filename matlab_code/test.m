function [frames, discriptors, count_point,training_label_vector] = test()
%APPLE_SIFT_TRAIN Summary of this function goes here
%   Detailed explanation goes here
%   many classes of pictures formatted as jpg, get their sift features for
%   next stage k-means and svm 


%   这里面training_label_vector是要用数字表示还是用字符串表示呢？数字更简单，但是需要字符串的对应
%   先用字符串试试看吧
count_point = [];
count = 0;
number = 0;%用在记录label里面的，表示的是图片的个数。
max_number = 100000;% 假定一个最大的图片个数 
training_label_vector = cell(1,max_number);%现在还不清楚有多少个图片呢，怎么办

mydir=uigetdir('d:/trainsift','选择一个目录'); %获得选择的文件夹
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);



for i=3:n  %因为1,2 里面分别是 . .. ,所以要从3开始
 if train_classes(i).isdir   %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)对应的是*.jpg
    class_len = length(pictures);
        
    for j = 3:class_len  %从3开始的原因与上面相同， 处理每个class里面的文件
       number = number +1; 
       %记录图片的label
        training_label_vector{1,number}=train_classes(i).name;
   
       %记录图片的label
       %name = [train_classes(i).name,pictures(j).name];
       %disp(name); 
            
       % 处理文件 在这个地方
       img_dir = [mydir,train_classes(i).name];
       img_path = fullfile(img_dir, pictures(j).name);
       image = imread(img_path);
       %imshow(image);
       tmp = single(rgb2gray(image)); % 转成灰度图
       image = tmp;
        %[F] = blockproc(image,[8,10],fun);
       [F,D]=vl_sift(image);    %sift特征
        num = size(F,2);
        count_point(number) = num;%之前这里出现问题，修复后测试值后边是带1的。
        if count == 0
            frames = F;
            discriptors = D;
        else
            frames(:,count+1:count+num) = F;
            discriptors(:,count+1:count+num) = D;
        end
        count = count + num;
        
       % 处理文件 在这个地方
    end
 end
end
count_point = count_point';
cd('D:/test_result');%结果保存在“D:/test_result”文件夹下面
save('test_apple_test_class4');
end


