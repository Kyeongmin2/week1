# 2023 겨울 몰입캠프 - 안드로이드 앱 개발 프로젝트

## 팀 멤버
- 강다희
- 이경민

## 프로젝트 소개

프로젝트는 안드로이드 앱을 개발하여 다양한 기능을 구현하고자 하는 목적으로 진행되었습니다. 기본적인 탭 구조를 활용하여 안드로이드 스튜디오에서 제공하는 Bottom Navigation Views Activity를 사용하여 개발되었습니다.

엡의 이름은 'PIB'로, 'Pink is Best'(전체적인 UI컨셉)과 'Personal Identity Book'(본인의 연락처, 이미지를 자유롭게 등록하고 가벼운 알람, 타이머, 스톱워치 기을 사용할 수 있는 개인 신분증 컨셉의 앱)이라는 중의적인 의미를 가졌습니다.   

## 탭 1 - 나의 연락처 구축

### 주요 기능

1. **RecyclerView를 통한 연락처 리스트 표시**
   - RecyclerView를 활용하여 연락처 리스트를 동적으로 생성하고 표시합니다.
   - <img src="https://github.com/Kyeongmin2/week1/assets/139040057/9617d53c-efa6-44e5-bd26-d18e724c6955" alt="Screenshot" width="300"/>
2. **연락처 추가 기능**
   - 사용자가 앱 내에서 새로운 연락처를 등록할 수 있도록 기능을 구현하였습니다.
   <img src="https://github.com/Kyeongmin2/week1/assets/139040057/e457d75a-ee99-4915-9661-91ebc1e56264" alt="Image 1" width="300"/>

3. **연락처 삭제 기능**
   - 사용자가 불필요한 연락처를 삭제할 수 있는 기능을 제공합니다.

4. **자동 정렬**
   - 연락처가 추가 또는 삭제될 때마다 리스트가 자동으로 이름순으로 정렬되도록 설계되었습니다.

5. **연락처 수정**
   - RecyclerView를 터치하면 연락처를 수정할 수 있는 창이 생성되도록 구현되었습니다.

6. **스와이프로 전화 연결**
   - 연락처를 오른쪽으로 스와이프하면 해당 연락처에 전화를 걸 수 있는 기능이 추가되었습니다.

## 탭 2 - 나만의 이미지 갤러리 구축

### 주요 기능

1. **이미지 RecyclerView 구현**
   - RecyclerView를 이용하여 이미지를 그리드 형태로 표시하였습니다.
   - <img src="https://github.com/Kyeongmin2/week1/assets/139040057/daefb90c-0340-4c9b-aefa-2de4b78b3951" alt="Image 2" width="300"/>

2. **로컬 갤러리 및 카메라 연동**
   - FloatingButton을 통해 로컬 갤러리와 카메라를 연동하여 이미지를 추가할 수 있는 기능을 제공합니다.
   - <img src="https://github.com/Kyeongmin2/week1/assets/139040057/72c4e8ae-2fd7-4cca-8bdb-e874c8974821" alt="Image" width="300"/>
  
3. **이미지 확대 기능**
   - 이미지를 가볍게 한번 누르면 확대해서 볼 수 있는 기능을 구현했습니다.
   - <img src="https://github.com/Kyeongmin2/week1/assets/139040057/ad0cda0c-f364-4144-8438-417b0a6ed164" alt="Image" width="300"/>

4. **이미지 삭제 기능**
   - 이미지를 꾹 누르면 삭제할 수 있는 기능을 구현했습니다.
   - <img src="https://github.com/Kyeongmin2/week1/assets/139040057/0834a47f-7f28-4399-840d-fa3e2f82ee79" alt="Image" width="300"/>


# 탭 3 - 알람, 타이머, 스톱워치 기능 구현
<img src="https://github.com/Kyeongmin2/week1/assets/139040057/dc282994-e71a-47a8-8014-7d5a47435197" alt="Image 3" width="300"/>

## 기능 소개

### 알람

- **알람 설정**
  - 가운데 시계를 터치하여 알람 시간을 설정할 수 있습니다.
   <img src="https://github.com/Kyeongmin2/week1/assets/139040057/ea2957e6-d8bc-4bed-a236-9ebe7abebf83" alt="Image 4" width="300"/>


- **알람 설정 및 제거**
  - SET 버튼을 누르면 설정한 알람이 등록되며, 목록 버튼을 통해 설정된 알람 목록을 확인할 수 있습니다.
  - <img src="https://github.com/Kyeongmin2/week1/assets/139040057/eb66c58e-4cf2-4054-bfba-599a5817fab7" alt="Image 5" width="300"/>
  - 알람 목록에서 특정 알람을 선택하여 제거할 수 있습니다.

### 타이머
<img src="https://github.com/Kyeongmin2/week1/assets/139040057/e698f84d-a7fc-49d0-a810-23402e96cfba" alt="Image 6" width="300"/>

- **타이머 설정**
  - 가운데 시, 분, 초 숫자를 조작하여 타이머 시간을 설정할 수 있습니다.

- **타이머 시작 및 일시정지**
  - 시작 버튼을 누르면 타이머가 시작되며, 동작 중에는 일시정지 버튼이 나타나 타이머를 일시 중지할 수 있습니다.

- **타이머 초기화**
  - 정지 버튼을 눌러 타이머를 초기화할 수 있습니다.

### 스톱워치

- **스톱워치 시작 및 정지**
  - 시작 버튼을 누르면 스톱워치가 시작되며, 동작 중에는 정지 버튼이 나타나 스톱워치를 정지할 수 있습니다.
    <img src="https://github.com/Kyeongmin2/week1/assets/139040057/e0c4ff7f-eeb6-4eae-86f4-c344c19a87f2" alt="Image 7" width="300"/>
    
- **스톱워치 초기화**
  - 초기화 버튼을 눌러 스톱워치를 초기화할 수 있습니다.

## 사용 방법

- **알람**
  1. 가운데 시계를 터치하여 알람 시간 설정
  2. SET 버튼으로 알람 등록
  3. 목록 버튼으로 알람 목록 확인
  4. 알람 목록에서 알람 제거

- **타이머**
  1. 가운데 시, 분, 초 숫자를 조작하여 타이머 시간 설정
  2. 시작 버튼으로 타이머 시작
  3. 동작 중에는 일시정지 버튼으로 타이머 일시 중지
  4. 정지 버튼으로 타이머 초기화

- **스톱워치**
  1. 시작 버튼으로 스톱워치 시작
  2. 동작 중에는 정지 버튼으로 스톱워치 정지
  3. 초기화 버튼으로 스톱워치 초기화
