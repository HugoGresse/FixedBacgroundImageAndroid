# FixedBacgroundImageAndroid
Display an image that appear to be fixed in position while the parent ScrollView is moving (like css background fixed). 

### Mechanism
The image top is moved every 10ms based on the parent view `locationOnScreen`. 

### Restriction:
- no listener attached to any parent view (so the image layout can be used as a library with simple api)
- ~25-30 fps

### Issue
The current mechanism have some glitch where the image jumb when scrolling. It only work when the scroll is very slow. It doesn't matter at which speed the top of the image is freshed, the glitch is still present. 
