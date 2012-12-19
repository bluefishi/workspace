function [c2]=getmfcc(wavpath)
%GETMFCC Summary of this function goes here
%   get the mfcc feature of a certain wav file.

max_len = 100000;%这个值也不知道设多大合适。。。
m = zeros(max_len,12);
five_m = 4800000;
one_m = 960000;

%wavpath = 'D:\trainmfcc\actor\8AHPAWACZow.wav'; 
%wavpath = 'D:\wav\one\arctic_0001.wav';
sizeinfo = wavread(wavpath, 'size'); 
tot_samples = sizeinfo(1);
%startpos = floor(tot_samples / 3);
%endpos = 2 * startpos;

if tot_samples < five_m          %if the audio is short than five minutes,and process all
    [x,fs]=wavread(wavpath);%'D:\wav\arctic_0001.wav'
else
    [x,fs]=wavread(wavpath,[one_m five_m]);%deal with the 1-5 minutes
end
    %[x, fs] = wavread(wavpath, [1 endpos]);
%设定mel滤波器系数
bank = melbankm(24,256,fs,0,0.4,'m');
bank = full(bank);
bank = bank/max(bank(:));
%设定DCT系数o
dct = zeros(12,24);
for k = 1:12
    n = 0:23;
    dct(k,:)=cos((2*n+1)*k*pi/(2*24));%另一个版本这里是dctcoef
end
%设置归一化的倒谱提升窗口
w = 1+6*sin(pi* [1:12]./12);
w = w/max(w);
%设置预加重滤波器
ss = double(x);
ss = filter([1-0.9375],1,ss);
%对语音信号进行分帧
ss = enframe (ss ,hamming(512), 256);%对s512点分为一帧,即32ms
%计算每一帧的mfcc参数
for i = 1:size(ss,1)
    s = ss(i,:);
   % s = s' .*hamming(256);
    %另一个版本里面设置了一个变量 s = s' .*hamming(256)
    %对信号s进行fft计算,快速傅里叶变换    
    t = abs(fft(s));
    t = t.^2;
%对fft参数进行mel滤波取对数再计算倒谱
    c1 = dct*log(bank*t(1:129)');%另一个版本这里是dctcoef
    c2 = c1.*w';
    %mfcc参数
    m(i,:)=c2';
end
m(size(ss,1)+1:100000,:)=[];
%计算mfcc参数的一阶差分,求取差分系数
dtm = zeros(size(m));
for i = 3:size(m,1)-2
    dtm(i,:) = -2*m(i-2,:) - m(i-1,:) + m(i+1,:) + 2*m(i+2,:);
end
dtm = dtm/3;
%合并mfcc参数和一阶差分参数
c = [m dtm];
%去除首尾两帧，因为这两帧一阶差分参数为0
c2 = c(3:size(m, 1)-2,:);
%所得的c2便是mfcc参数，也就是声纹识别的特征参数。

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%另一个版本下面还有一点
%subplot(211)
%c2_1=c2(:,1);
%plot(c2_1);
%title('MFCC');
%ylabel('幅值');
%[h,w]=size(c2);
%A=size(c2);
%subplot(212)
%plot([1,w],A);
%xlabel('维数');
%ylabel('幅值');
%title('维数于幅值的关系');


%save('mfcc_feature0','c2');%let the caller save the parameters
end

