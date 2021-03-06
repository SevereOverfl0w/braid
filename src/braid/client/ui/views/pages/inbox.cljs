(ns braid.client.ui.views.pages.inbox
  (:require [braid.client.state :refer [subscribe]]
            [reagent.ratom :include-macros true :refer-macros [reaction]]
            [braid.client.ui.views.threads :refer [threads-view]]
            [braid.client.dispatcher :refer [dispatch!]]))

(defn clear-inbox-button-view []
  (let [group-id (subscribe [:open-group-id])
        open-threads (subscribe [:open-threads] [group-id])]
    (fn []
      [:div.clear-inbox
        (when (< 5 (count @open-threads))
          [:button {:on-click (fn [_]
                                (dispatch! :clear-inbox))}
            "Clear Inbox"])])))

(defn inbox-page-view
  []
  (let [user-id (subscribe [:user-id])
        group (subscribe [:active-group])
        group-id (subscribe [:open-group-id])
        open-threads (subscribe [:open-threads] [group-id])
        sorted-threads (reaction
                         (->> @open-threads
                              ; sort by last message sent by logged-in user, most recent first
                              (sort-by
                                (comp (partial apply max)
                                      (partial map :created-at)
                                      (partial filter (fn [m] (= (m :user-id) @user-id)))
                                      :messages))
                              reverse))]
    (fn []
      [:div.page.inbox
       [:div.title "Inbox"]
       [:div.intro
        (:intro @group)
        [clear-inbox-button-view]]
       [threads-view {:new-thread-args {}
                      :group-id @group-id
                      :threads @sorted-threads}]])))
