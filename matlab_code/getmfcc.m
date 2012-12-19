function [c2]=getmfcc(wavpath)
%GETMFCC Summary of this function goes here
%   get the mfcc feature of a certain wav file.

max_len = 100000;%���ֵҲ��֪��������ʡ�����
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
%�趨mel�˲���ϵ��
bank = melbankm(24,256,fs,0,0.4,'m');
bank = full(bank);
bank = bank/max(bank(:));
%�趨DCTϵ��o
dct = zeros(12,24);
for k = 1:12
    n = 0:23;
    dct(k,:)=cos((2*n+1)*k*pi/(2*24));%��һ���汾������dctcoef
end
%���ù�һ���ĵ�����������
w = 1+6*sin(pi* [1:12]./12);
w = w/max(w);
%����Ԥ�����˲���
ss = double(x);
ss = filter([1-0.9375],1,ss);
%�������źŽ��з�֡
ss = enframe (ss ,hamming(512), 256);%��s512���Ϊһ֡,��32ms
%����ÿһ֡��mfcc����
for i = 1:size(ss,1)
    s = ss(i,:);
   % s = s' .*hamming(256);
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
m(size(ss,1)+1:100000,:)=[];
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
%subplot(211)
%c2_1=c2(:,1);
%plot(c2_1);
%title('MFCC');
%ylabel('��ֵ');
%[h,w]=size(c2);
%A=size(c2);
%subplot(212)
%plot([1,w],A);
%xlabel('ά��');
%ylabel('��ֵ');
%title('ά���ڷ�ֵ�Ĺ�ϵ');


%save('mfcc_feature0','c2');%let the caller save the parameters
end

