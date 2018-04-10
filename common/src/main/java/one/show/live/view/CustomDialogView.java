//package one.show.live.view;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.TextView;
//
//import one.show.live.util.FixKitkatDialogUtils;
//import one.show.live.util.StringUtils;
//import one.show.live.common.R;
//
//
//public class CustomDialogView extends Dialog {
//
//	private boolean isFixKitkat;
//	public CustomDialogView(Context context, int theme) {
//		super(context, theme);
//	}
//
//	public CustomDialogView(Context context) {
//		super(context);
//	}
//
//	/**
//	 * Helper class for creating a custom dialog
//	 */
//	public static class Builder {
//
//		private Context context;
//		private int style;
//		private String title;//标题
//		private String message;//信息
//		private String letButtonText;//左边按钮
//		private String rightButtonText;//右边按钮
//
//		private Button leftButton;
//		private Button rightButton;
//		private boolean isFix;
//
//		private OnClickListener leftButtonClickListener, rightButtonClickListener;
//
//		public Builder(Context context) {
//			this.context = context;
//			this.style=0;
//		}
//
//		public Builder(Context context,int style) {
//			this.context = context;
//			this.style=style;
//		}
//
//		public Builder setMessage(String message) {
//			this.message = message;
//			return this;
//		}
//
//		public Builder setIsFix(boolean isFix) {
//			this.isFix = isFix;
//			return this;
//		}
//
//		public Builder setMessage(int message) {
//			this.message = (String) context.getText(message);
//			return this;
//		}
//
//		public Builder setTitle(int title) {
//			this.title = (String) context.getText(title);
//			return this;
//		}
//
//		public Builder setTitle(String title) {
//			this.title = title;
//			return this;
//		}
//
//		public Builder setLeftButton(int letButtonText, OnClickListener listener) {
//			this.letButtonText = (String) context.getText(letButtonText);
//			this.leftButtonClickListener = listener;
//			return this;
//		}
//
//		public Builder setLeftButton(String letButtonText, OnClickListener listener) {
//			this.letButtonText = letButtonText;
//			this.leftButtonClickListener = listener;
//			return this;
//		}
//
//
//		public Builder setRightButton(int rightButtonText) {
//			return setRightButton(rightButtonText,null);
//		}
//
//		public Builder setRightButton(String rightButtonText) {
//			return setRightButton(rightButtonText,null);
//		}
//
//
//		public Builder setRightButton(int rightButtonText, OnClickListener listener) {
//			this.rightButtonText = (String) context.getText(rightButtonText);
//			this.rightButtonClickListener = listener;
//			return this;
//		}
//
//		public Builder setRightButton(String rightButtonText, OnClickListener listener) {
//			this.rightButtonText = rightButtonText;
//			this.rightButtonClickListener = listener;
//			return this;
//		}
//
//		/**
//		 * 返回实例
//		 */
//		public CustomDialogView create() {
//			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			final CustomDialogView dialog;
//
//			if(style==0){
//				dialog = new CustomDialogView(context, R.style.MyDialogStyle);
//			}else{
//				dialog = new CustomDialogView(context, style);
//			}
//			dialog.isFixKitkat = isFix;
//			View layout = inflater.inflate(R.layout.custom_dialog, null);
//			if(isFix){
//				FixKitkatDialogUtils.fixDialogPre(dialog);
//			}
//			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//			((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
//			leftButton = ((Button) layout.findViewById(R.id.dialog_left_buton));
//			rightButton = ((Button) layout.findViewById(R.id.dialog_right_buton));
//			if (letButtonText != null) {
//				leftButton.setText(letButtonText);
//				leftButton.setOnClickListener(new View.OnClickListener() {
//						public void onClick(View v) {
//							if (leftButtonClickListener != null) {
//								leftButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
//							}
//							else {
//								dialog.dismiss();
//							}
//						}
//					});
//			} else {
//				leftButton.setVisibility(View.GONE);
//			}
//			if (rightButtonText != null) {
//				rightButton.setText(rightButtonText);
//
//					rightButton.setOnClickListener(new View.OnClickListener() {
//						public void onClick(View v) {
//							if (rightButtonClickListener != null) {
//								rightButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
//							}else{
//								dialog.dismiss();
//							}
//						}
//					});
//			} else {
//				rightButton.setVisibility(View.GONE);
//				layout.findViewById(R.id.button_line).setVisibility(View.GONE);
//			}
//
//			if(StringUtils.isEmpty(title)||StringUtils.isEmpty(message)){
//				layout.findViewById(R.id.titleMsgLine).setVisibility(View.GONE);
//			}
//
//			if (StringUtils.isNotEmpty(title)) {
//				((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
//			}else{
//				((TextView) layout.findViewById(R.id.dialog_title)).setVisibility(View.GONE);
//			}
//
//
//
//			if (StringUtils.isNotEmpty(message)) {
//				((TextView) layout.findViewById(R.id.dialog_message)).setText(message);
//			} else {
//				((TextView) layout.findViewById(R.id.dialog_message)).setVisibility(View.GONE);
//			}
//			dialog.setContentView(layout);
//			return dialog;
//		}
//
//	}
//
//	@Override
//	public void show() {
//		super.show();
//		if(isFixKitkat) {
//			FixKitkatDialogUtils.fixDialogPost(this);
//		}
//	}
//}
