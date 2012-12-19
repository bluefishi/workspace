function [frames, discriptors, count_point,training_label_vector] = test()
%APPLE_SIFT_TRAIN Summary of this function goes here
%   Detailed explanation goes here
%   many classes of pictures formatted as jpg, get their sift features for
%   next stage k-means and svm 


%   ������training_label_vector��Ҫ�����ֱ�ʾ�������ַ�����ʾ�أ����ָ��򵥣�������Ҫ�ַ����Ķ�Ӧ
%   �����ַ������Կ���
count_point = [];
count = 0;
number = 0;%���ڼ�¼label����ģ���ʾ����ͼƬ�ĸ�����
max_number = 100000;% �ٶ�һ������ͼƬ���� 
training_label_vector = cell(1,max_number);%���ڻ�������ж��ٸ�ͼƬ�أ���ô��

mydir=uigetdir('d:/trainsift','ѡ��һ��Ŀ¼'); %���ѡ����ļ���
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);



for i=3:n  %��Ϊ1,2 ����ֱ��� . .. ,����Ҫ��3��ʼ
 if train_classes(i).isdir   %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)��Ӧ����*.jpg
    class_len = length(pictures);
        
    for j = 3:class_len  %��3��ʼ��ԭ����������ͬ�� ����ÿ��class������ļ�
       number = number +1; 
       %��¼ͼƬ��label
        training_label_vector{1,number}=train_classes(i).name;
   
       %��¼ͼƬ��label
       %name = [train_classes(i).name,pictures(j).name];
       %disp(name); 
            
       % �����ļ� ������ط�
       img_dir = [mydir,train_classes(i).name];
       img_path = fullfile(img_dir, pictures(j).name);
       image = imread(img_path);
       %imshow(image);
       tmp = single(rgb2gray(image)); % ת�ɻҶ�ͼ
       image = tmp;
        %[F] = blockproc(image,[8,10],fun);
       [F,D]=vl_sift(image);    %sift����
        num = size(F,2);
        count_point(number) = num;%֮ǰ����������⣬�޸������ֵ����Ǵ�1�ġ�
        if count == 0
            frames = F;
            discriptors = D;
        else
            frames(:,count+1:count+num) = F;
            discriptors(:,count+1:count+num) = D;
        end
        count = count + num;
        
       % �����ļ� ������ط�
    end
 end
end
count_point = count_point';
cd('D:/test_result');%��������ڡ�D:/test_result���ļ�������
save('test_apple_test_class4');
end


