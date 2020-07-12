package com.example.djikon;
import com.example.Success;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetToken {
        @SerializedName("success")
        @Expose
        private Success success;

        public Success getSuccess() {
            return success;
        }

        public void setSuccess(Success success) {
            this.success = success;
        }

    }

