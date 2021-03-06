(ns braid.client.ui.views.error-banner
  (:require [braid.client.state :refer [subscribe]]
            [braid.client.dispatcher :refer [dispatch!]]))

(defn error-banner-view []
  (let [errors (subscribe [:errors])]
    (fn []
      [:div.error-banners
       (doall
         (for [[err-key err cls] @errors]
           ^{:key err-key}
           [:div.error-banner
            {:class cls}
            err
            [:span.close
             {:on-click (fn [_] (dispatch! :clear-error err-key))}
             "×"]]))])))
