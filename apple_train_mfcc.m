function [ mfcc_features,label_vector,count_point ] = apple_train_mfcc( )
%apple_train_mfcc Summary of this function goes here
%   Detailed explanation goes here

mydir=uigetdir('d:/trainmfcc','ѡ��һ��Ŀ¼'); %���ѡ����ļ���
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);

count_point = [];
count = 0;
number = 0;%���ڼ�¼label����ģ���ʾ����ͼƬ�ĸ�����
max_len = 200;%���������
label_vector = zeros(1,max_len);



for i=3:n  %��Ϊ1,2 ����ֱ��� . .. ,����Ҫ��3��ʼ
% if (train_classes(i).isdir && strcmp(train_classes(i).name,'.')&& strcmp(train_classes(i).name,'..')) %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)��Ӧ����*.jpg
    class_len = length(pictures);
        
    for j = 1:class_len  %��3��ʼ��ԭ����������ͬ�� ����ÿ��class������ļ�
        img_dir = [mydir,train_classes(i).name];
       if(~strcmp(pictures(j).name,'.')&& ~strcmp(pictures(j).name,'..'))
        img_path = fullfile(img_dir, pictures(j).name);
        
        number = number +1; 
       %��¼ͼƬ��label
       
        label_vector(number) = i-2;
   
       %��¼ͼƬ��label
       %name = [train_classes(i).name,pictures(j).name];
       %disp(name); 
            
       % �����ļ� ������ط�
     
       mfcc = getmfcc(img_path);
        num = size(mfcc,1);
        count_point(number) = num;%֮ǰ����������⣬�޸������ֵ����Ǵ�1�ġ�
        if count == 0
            mfcc_features = mfcc;
        else
            mfcc_features(count+1:count+num,:) = mfcc;
          
        end
        count = count + num;
        
       % �����ļ� ������ط�
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



