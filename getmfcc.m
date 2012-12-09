function [output]=getmfcc()
%GETMFCC Summary of this function goes here
%   Detailed explanation goes here

[x,fs]=wavread('D:\wav\arctic_0001.wav');
%�趨mel�˲���ϵ��
bank = melbankm(24,256,fs,0,0.4,'m');
bank = full(bank);
bank = bank/max(bank(:));
%�趨DCTϵ��
for k = 1:12
    n = 0:23;
    dct(k,:)=cos((2*n+1)*k*pi/(2*24));%��һ���汾������dctcoef
end
%���ù�һ���ĵ�����������
w = 1+6*sin(pi*[1:12]./12);
w = w/max(w);
%����Ԥ�����˲���
ss = double(x);
ss = filter([1-0.9375],1,ss);
%�������źŽ��з�֡
ss = enframe (ss ,hamming(256), 128);%��s256���Ϊһ֡
%����ÿһ֡��mfcc����
for i = 1:size(ss,1)
    s = ss(i,:);
    %��һ���汾����������һ������ s = s' .*hamming(256)
    %���ź�s����fft����,���ٸ���Ҷ�任    
    t = abs(fft(s));
    t = t.^2;
%��fft��������mel�˲�ȡ�����ټ��㵹��
    c1 = dct*log(bank*t(1:129)');%��һ���汾������dctcoef
    c2 = c1.*w';
    %mfcc����
    m(i,:)=c2';
end
%����mfcc������һ�ײ��,��ȡ���ϵ��
dtm = zeros(size(m));
for i = 3:size(m,1)-2
    dtm(i,:) = -2*m(i-2,:) - m(i-1,:) + m(i+1,:) + 2*m(i+2,:);
end
dtm = dtm/3;
%�ϲ�mfcc������һ�ײ�ֲ���
c = [m dtm];
%ȥ����β��֡����Ϊ����֡һ�ײ�ֲ���Ϊ0
c2 = c(3:size(m, 1)-2,:);
%���õ�c2����mfcc������Ҳ��������ʶ�������������

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%��һ���汾���滹��һ��
subplot(211)
c2_1=c2(:,1);
plot(c2_1);
title('MFCC');
ylabel('��ֵ');
[h,w]=size(c2);
A=size(c2);
subplot(212)
plot([1,w],A);
xlabel('ά��');
ylabel('��ֵ');
title('ά���ڷ�ֵ�Ĺ�ϵ');
save('mfcc_feature','c2');
end

