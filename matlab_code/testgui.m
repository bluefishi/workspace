function varargout = testgui(varargin)
% TESTGUI MATLAB code for testgui.fig
%      TESTGUI, by itself, creates a new TESTGUI or raises the existing
%      singleton*.
%
%      H = TESTGUI returns the handle to a new TESTGUI or the handle to
%      the existing singleton*.
%
%      TESTGUI('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in TESTGUI.M with the given input arguments.
%
%      TESTGUI('Property','Value',...) creates a new TESTGUI or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before testgui_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to testgui_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help testgui

% Last Modified by GUIDE v2.5 19-Dec-2012 11:56:41

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @testgui_OpeningFcn, ...
                   'gui_OutputFcn',  @testgui_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before testgui is made visible.
function testgui_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to testgui (see VARARGIN)

% Choose default command line output for testgui
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes testgui wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = testgui_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in init_sift.
function init_sift_Callback(hObject, eventdata, handles)
[path,message]=vl_setup();
set(handles.text1,'String',message);

% hObject    handle to init_sift (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in sift_go.
function sift_go_Callback(hObject, eventdata, handles)
% hObject    handle to sift_go (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
%test();

count_point = [];%这个量是全局的，对一次选中的文件夹下面所有的图片，记录每个图提取出的sift点个数
pic_num_max = 50;%设定参数，每个类里面最多处理的图片数量
number = 0;%全局量，表示的是所有处理的图片数。
training_label_vector=zeros();%全局量，所有处理的图片的标签
shownames = [];%实时展示正在处理的视频名字

%初始文件夹，需要设定，现在为了测试就设为这个方便
mydir=uigetdir('H:/温建娇/smalltest','选择一个目录'); %获得选择的文件夹
cd(mydir);
if mydir(end)~='\'
 mydir=[mydir,'\'];
end
train_classes = dir(mydir);
n=length(train_classes);
%进度条
h=waitbar(0,'please wait...');

for i=3:n  %因为1,2 里面分别是 . .. ,所以要从3开始
    %进度条，因为现在不知道总共的图片的个数，所以只能以类的个数作为进度评估标准
     waitbar(i/n,h);
    
     %因为把每个类的sift分别存起来，所以在这里要初始化
    count = 0;%记每个类中图片数
    frames = [];
    discriptors = [];
 if train_classes(i).isdir   %train_class(i) should be folder which contains jpgs 
    pictures = dir(train_classes(i).name);  %pictures(j)对应的是*.jpg
    class_len = length(pictures);
    %处理一下，图片太多就忽略后边的
    if(class_len > pic_num_max)
        class_len = pic_num_max;
    end
    
    for j = 3:class_len  %从3开始的原因与上面相同， 处理每个class里面的文件
       number = number +1; 
       %记录图片的label
       training_label_vector(number)=i-2;   
       %记录图片的label
                
       % 处理文件 在这个地方 
       img_dir = [mydir,train_classes(i).name];
       img_path = fullfile(img_dir, pictures(j).name);
       disp(pictures(j).name);
         
       shownames = [shownames,pictures(j).name,char(13,10)'];
       str=sprintf('processing:  %s',shownames);
       set(handles.text1,'String',str);
    
       image = imread(img_path);
       tmp = single(rgb2gray(image)); % 转成灰度图
       image = tmp;
        %[F] = blockproc(image,[8,10],fun);
       [F,D]=vl_sift(image);    %sift特征
        num = size(F,2);
        count_point(number) = num;
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
 %把一个类的sift特征保存成一个文件
 %name1=[train_classes(i).name,'f'];%这里存的也是有问题
 %name2=[train_classes(i).name,'d'];
 %save(name1,'frames');
 %save(name2,'discriptors');
end
 close(h);
training_label_vector = training_label_vector';
count_point = count_point';

%结果的保存路径应该由外面定义比较合适
%name3 = [mydir,'training_label_vector'];
%name4 = [mydir,'count_point'];
%cd('H:/温建娇/feature_data/sift');%结果保存在“D:/test_result”文件夹下面
%save(name3,'training_label_vector');
%save(name4,'count_point');


% --- Executes on button press in click.
function click_Callback(hObject, eventdata, handles)
% hObject    handle to click (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
persistent c
if isempty(c)
     c=0;
end
c=c+1;
str=sprintf('Total Clicks:  %d',c);
set(handles.text1,'String',str);


% --- Executes on button press in show_text.
function show_text_Callback(hObject, eventdata, handles)
% hObject    handle to show_text (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
persistent s
if isempty(s)
     s='hello';
end

 
s=[s,'haha'];
str=sprintf('Total Clicks:  %s',s);
set(handles.text1,'String',str);
s='you';
