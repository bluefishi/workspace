function [output]=getmfcc()
%GETMFCC Summary of this function goes here
%   Detailed explanation goes here

[x,fs]=wavread('D:\wav\arctic_0001.wav');
%设定mel滤波器系数
bank = melbankm(24,256,fs,0,0.4,'m');
bank = full(bank);
bank = bank/max(bank(:));
%设定DCT系数
for k = 1:12
    n = 0:23;
    dct(k,:)=cos((2*n+1)*k*pi/(2*24));%另一个版本这里是dctcoef
end
%设置归一化的倒谱提升窗口
w = 1+6*sin(pi*[1:12]./12);
w = w/max(w);
%设置预加重滤波器
ss = double(x);
ss = filter([1-0.9375],1,ss);
%对语音信号进行分帧
ss = enframe (ss ,hamming(256), 128);%对s256点分为一帧
%计算每一帧的mfcc参数
for i = 1:size(ss,1)
    s = ss(i,:);
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
subplot(211)
c2_1=c2(:,1);
plot(c2_1);
title('MFCC');
ylabel('幅值');
[h,w]=size(c2);
A=size(c2);
subplot(212)
plot([1,w],A);
xlabel('维数');
ylabel('幅值');
title('维数于幅值的关系');
save('mfcc_feature','c2');
end

