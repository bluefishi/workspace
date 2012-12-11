function [ mfcc_features,label_vector,count_point ] = apple_train_mfcc( )
%apple_train_mfcc Summary of this function goes here
%   Detailed explanation goes here

mydir=uigetdir('d:/trainmfcc','选择一个目录'); %获得选择的文件夹
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);

count_point = [];
count = 0;
number = 0;%用在记录label里面的，表示的是图片的个数。
max_len = 200;%类的最大个数
label_vector = zeros(1,max_len);



for i=3:n  %因为1,2 里面分别是 . .. ,所以要从3开始
% if (train_classes(i).isdir && strcmp(train_classes(i).name,'.')&& strcmp(train_classes(i).name,'..')) %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)对应的是*.jpg
    class_len = length(pictures);
        
    for j = 1:class_len  %从3开始的原因与上面相同， 处理每个class里面的文件
        img_dir = [mydir,train_classes(i).name];
       if(~strcmp(pictures(j).name,'.')&& ~strcmp(pictures(j).name,'..'))
        img_path = fullfile(img_dir, pictures(j).name);
        
        number = number +1; 
       %记录图片的label
       
        label_vector(number) = i-2;
   
       %记录图片的label
       %name = [train_classes(i).name,pictures(j).name];
       %disp(name); 
            
       % 处理文件 在这个地方
     
       mfcc = getmfcc(img_path);
        num = size(mfcc,1);
        count_point(number) = num;%之前这里出现问题，修复后测试值后边是带1的。
        if count == 0
            mfcc_features = mfcc;
        else
            mfcc_features(count+1:count+num,:) = mfcc;
          
        end
        count = count + num;
        
       % 处理文件 在这个地方
       end
     end
  %  end
  %  end
 end
 


 label_vector(number+1:max_len)=[];
count_point = count_point';
%cd('D:\test_result');
%save('apple_train_mfcc');
end



